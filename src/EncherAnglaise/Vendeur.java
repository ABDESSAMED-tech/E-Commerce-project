package EncherAnglaise;

import static EncherAnglaise.EncherForme.Text;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Vendeur extends Agent {

    public static int startBid = 0;
    public static int bid = 0;
    public static String bidedAgent = "";
    public static String lastBidedAgent = "";
    static float lastPrix = 0;

    private float VendPrixInit;
    private float VendPrixRes;
    private String Nom_Prod;
    private int NbrItr;
    ArrayList<String> ListeNomAcheteurs;
    float prix;

    protected void setup() {
        Object[] ArgsVend = getArguments();

        /*for (int i=0;i<ArgsVend.length;i++) {
			System.out.println(this.getAID().getLocalName()+":"+ArgsVend[i]);
		}*/
        VendPrixInit = Float.valueOf(String.valueOf(ArgsVend[0])).floatValue();
        VendPrixRes = Float.valueOf(String.valueOf(ArgsVend[1])).floatValue();
        Nom_Prod = String.valueOf(ArgsVend[2]);
        NbrItr = Integer.valueOf(String.valueOf(ArgsVend[3])).intValue();
        ListeNomAcheteurs = (ArrayList<String>) ArgsVend[4];

        ArrayList<String> Liste = ListeNomAcheteurs;
        prix = VendPrixInit;

        for (int i = 0; i < Liste.size(); i++) {
            ACLMessage Vmsg = new ACLMessage(ACLMessage.INFORM);
            Vmsg.addReceiver(new AID(Liste.get(i), AID.ISLOCALNAME));
            Vmsg.setContent(String.valueOf(prix));
            //EncherForme.Text = EncherForme.Text + "\n" + EncherForme.nomeavend + "  j'ai envoyer le nouveau prix 11  " + prix + " a l'acheteur acheteur " + Liste.get(i);
            //EncherForme.ExecutionVend.append("\n" + EncherForme.Text);

            System.out.print(EncherForme.nomeavend + " j'ai envoyer le nouveau prix " + prix + " a l'acheteur " + Liste.get(i) + "print vved 1" + "\n");
            EncherForme.ExecutionVend.append("\n" + EncherForme.nomeavend + ": j'ai envoyer le nouveau prix " + prix + " a l'acheteur " + Liste.get(i));
            send(Vmsg);
            if (i == 2) {
                startBid = 1;
            }
        }

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                // TODO Auto-generated method stub
                ACLMessage Amsg = receive();

                if (Amsg != null) {
                    if (Amsg.getContent().equals("-1")) {
                        for (int i = 0; i < Liste.size(); i++) {
                            if (Amsg.getSender().getLocalName().equals(Liste.get(i))) {
                                Liste.remove(i);
                            }
                        }

                        for (int i = 0; i < Liste.size(); i++) {
                            if (!Liste.get(i).equals(bidedAgent)) {
                                ACLMessage Vmsg = new ACLMessage(ACLMessage.INFORM);
                                Vmsg.addReceiver(new AID(Liste.get(i), AID.ISLOCALNAME));
                                Vmsg.setContent(String.valueOf(prix));
                                // EncherForme.Text = EncherForme.Text + "\n" + EncherForme.nomeavend + ": j'ai envoyer le nouveau prix " + prix + " a l'" + Liste.get(i);
                                // EncherForme.ExecutionVend.append("\n" + "Vendeur : j'ai envoyer le nouveau prix " + prix + " a l'acheteur " + Liste.get(i));

                                System.out.print("\n" + EncherForme.nomeavend + ": j'ai envoyer le nouveau prix  " + prix + " a l'acheteur " + Liste.get(i) + "print vved 2" + "\n");
                                EncherForme.ExecutionVend.append("\n" + EncherForme.nomeavend + ": j'ai envoyer le nouveau prix  " + prix + " a l'acheteur " + Liste.get(i));
                                send(Vmsg);
                            }
                        }
                        bid = 0;
                    } else {

                        prix = Float.valueOf(Amsg.getContent()).floatValue();
                        lastPrix = prix;
                        for (int i = 0; i < Liste.size(); i++) {
                            if (!Liste.get(i).equals(bidedAgent)) {
                                if (Liste.size() != 1) {
                                    ACLMessage Vmsg = new ACLMessage(ACLMessage.INFORM);
                                    Vmsg.addReceiver(new AID(Liste.get(i), AID.ISLOCALNAME));
                                    Vmsg.setContent(String.valueOf(prix));
                                    //EncherForme.Text = EncherForme.Text + "\n" + EncherForme.nomeavend + "j'ai envoyer le nouveau prix 33 " + prix + " a l'acheteur " + Liste.get(i);

                                    //EncherForme.ExecutionVend.append("\n" + EncherForme.Text);
                                    System.out.print("\n" + "Vendeur : j'ai envoyer le nouveau prix " + prix + " a l'acheteur " + Liste.get(i) + "print vved 3" + "\n");
                                    EncherForme.ExecutionVend.append("\n" + EncherForme.nomeavend + ": j'ai envoyer le nouveau prix " + prix + " a l'acheteur " + Liste.get(i));
                                    send(Vmsg);
                                } else {
                                    System.out.println("gagnant" + lastBidedAgent);
                                }
                            }
                        }
                        bid = 0;
                    }
                } else {
                    block();
                }
            }

        });

    }
}
