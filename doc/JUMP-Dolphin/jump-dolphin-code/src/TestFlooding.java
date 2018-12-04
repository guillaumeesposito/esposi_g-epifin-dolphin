/*
 *      _   _   _       ___  ___   _____            ___       ___  ___   _____
 *     | | | | | |     /   |/   | |  _  \          /   |     /   |/   | /  ___/
 *     | | | | | |    / /|   /| | | |_| |         / /| |    / /|   /| | | |___
 *  _  | | | | | |   / / |__/ | | |  ___/        / / | |   / / |__/ | | \___  \
 * | |_| | | |_| |  / /       | | | |           / /  | |  / /       | |  ___| |
 * \_____/ \_____/ /_/        |_| |_|          /_/   |_| /_/        |_| /_____/
 *
 *
 * Jump Asset Management Solution Jump Informatique. Tous droits réservés.
 * Ce programme est protégé par la loi relative au droit d'auteur et par les conventions internationales.
 * Toute reproduction ou distribution partielle ou totale du logiciel, par quelque moyen que ce soit, est
 * strictement interdite. Toute personne ne respectant pas ces dispositions se rendra coupable du délit de
 * contrefaçon et sera passible des sanctions pénales prévues par la loi.
 * daté du 20/07/2017.
 */

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public final class TestFlooding{
  public static void main(String[] args) throws IOException, URISyntaxException {

    RESTManager locRESTManager = new RESTManager(
            RESTManager.HOST_NAME,
            RESTManager.PORT,
            RESTManager.USERNAME_USER1,
            RESTManager.PASSWORD_USER1);
   // locRESTManager.getAssetList();
    locRESTManager.getRatioList();

    //Modification de la composition du portefeuille propriétaire
    Portfolio locNewPtf = locRESTManager.getPortfolio(RESTManager.ID_PTF_USER_1);
    System.out.println(locNewPtf);
    locRESTManager.pushPortfolio(RESTManager.ID_PTF_USER_1, locNewPtf);
    locNewPtf = locRESTManager.getPortfolio(RESTManager.ID_PTF_USER_1);
    System.out.println(locNewPtf);
    //Tentative d'ajotu d'un nouveau portefeuille
    //locRESTManager.addPtf(RESTManager.createPtfUser("NEW", Arrays.asList(12, 54, 6)));

    //Modification de la composition d'un portefeuille non possédé
    //locRESTManager.printPtfCompo(RESTManager.ID_PTF_USER_2);
    //final Portfolio locNewPt2 = RESTManager.createPtfUser(RESTManager.USERNAME_USER2, Arrays.asList(315, 405, 61));
    //locRESTManager.pushPortfolio(RESTManager.ID_PTF_USER_2, locNewPt2);

    //locRESTManager.printPtfCompo(RESTManager.ID_PTF_USER_2);

    //Test sharpe ratioment
    //FIXME : Possiblement long au lance si cache serveur pas initialisé
    //locRESTManager.invokeSharpeRatio(Arrays.asList(RESTManager.ID_PTF_USER_1), "2015-01-01", "2017-01-01");
  }


  public class SimpleRequestThread implements Runnable {
    public void run() {
      RESTManager locRESTManager = new RESTManager(
              RESTManager.HOST_NAME,
              RESTManager.PORT,
              RESTManager.USERNAME_USER1,
              RESTManager.PASSWORD_USER1);
     // locRESTManager.getRatioList();
    }
  }
}
