package EncherAnglaise;

import static EncherAnglaise.EncherForme.Text;
import java.util.concurrent.Semaphore;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Acheteur extends Agent {

    private String Nom_Vend;
    private float VendPrixInit;
    private float Mise;
    private float MaxPrix;
    private int NbrAch;
    String Newligne = System.getProperty("line.separator");
    float prix;
    static Semaphore semaphore = new Semaphore(1);
    float MaxMise;
   
    protected void setup() {
        Object[] ArgsVend = getArguments();

        /*for (int i=0;i<ArgsVend.length;i++) {
			System.out.println(this.getAID().getLocalName()+"i ="+i+""+":"+ArgsVend[i]);
		}*/
        Object[] ArgsAch = getArguments();
        Nom_Vend = String.valueOf(ArgsAch[0]);
        VendPrixInit = Float.valueOf(String.valueOf(ArgsAch[1])).floatValue();
        Mise = Float.valueOf(String.valueOf(ArgsAch[2])).floatValue();
        MaxPrix = Float.valueOf(String.valueOf(ArgsAch[3])).floatValue();
        NbrAch = Integer.valueOf(String.valueOf(ArgsAch[5])).intValue();
        // EncherForme.ExecutionVend.append("\n" + getLocalName() + " : Mon prix max est " + MaxPrix + " et ma mise est :" + Mise);
        // EncherForme.Text = EncherForme.Text + "\n" + getLocalName() + " : Mon prix max est " + MaxPrix + " et ma mise est :" + Mise;
        System.out.println(getLocalName() + " : Mon prix max est " + MaxPrix + " et ma mise est :" + Mise + "print 1");
        EncherForme.ExecutionVend.append("\n" + getLocalName() + " : Mon prix max est " + MaxPrix + " et ma mise est :" + Mise);
        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                // TODO Auto-generated method stub

                ACLMessage message = receive();

                if (message != null) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (EncherAnglaise.Vendeur.bid == 0) {
                        EncherAnglaise.Vendeur.bid = 1;
                        semaphore.release();

                        prix = Float.valueOf(message.getContent()).floatValue();

                        ACLMessage Amsg = new ACLMessage(ACLMessage.INFORM);
                        Amsg.addReceiver(new AID(Nom_Vend, AID.ISLOCALNAME));

                        if ((prix + Mise) < MaxPrix || prix <= MaxPrix) {
                            if ((prix + Mise) < MaxPrix) {
                                Amsg.setContent(String.valueOf(prix + Mise));
                                send(Amsg);
                            } else {
                                System.out.println(getLocalName() + " " + "Max Prix " + MaxPrix);
                                Amsg.setContent(String.valueOf(MaxPrix));
                                send(Amsg);
                            }
                            System.out.println(getLocalName() + " : j'ai proposer le prix suivant :" + Amsg.getContent() + "print 2");
                            EncherForme.ExecutionVend.append("\n" + getLocalName() + " : j'ai proposer le prix suivant :" + Amsg.getContent());
                            EncherAnglaise.Vendeur.lastBidedAgent = EncherAnglaise.Vendeur.bidedAgent;
                            EncherAnglaise.Vendeur.bidedAgent = getLocalName();

                        } else {
                            //EncherForme.Text = EncherForme.Text + "\n" + getLocalName() + " : je sors de l'enchere";
                            //EncherForme.ExecutionVend.append("\n" + getLocalName() + " : je sors de l'enchere");
                            System.out.println("\n" + getLocalName() + " : je sors de l'enchere" + "print 3");
                            EncherForme.ExecutionVend.append("\n" + getLocalName() + " : je sors de l'enchere");
                            Amsg.setContent("-1");
                            send(Amsg);
                            doDelete();
                            semaphore.release();
                        }
                    } else {
                        semaphore.release();
                    }
                } else {
                    block();
                }
            }

        });

    }

}
