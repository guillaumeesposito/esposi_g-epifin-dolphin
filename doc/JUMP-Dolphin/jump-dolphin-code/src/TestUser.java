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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exemple de code d'un utilisateur
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public final class TestUser {
  public static void main(String[] args) throws IOException, URISyntaxException {
    RESTManager locRESTManager = new RESTManager(
            RESTManager.HOST_NAME,
            RESTManager.PORT,
            RESTManager.USERNAME_USER1,
            RESTManager.PASSWORD_USER1);

    System.out.println("\n**** Liste des actifs ****");
    List<Asset> locAssetList = locRESTManager.getAssetList("2012-01-01");
    if (locAssetList == null) {
      return;
    }
    locAssetList.forEach(parAsset -> System.out.println(parAsset));

    System.out.println("\n**** Liste des ratios ****");
    List<Ratio> locRatioList = locRESTManager.getRatioList();
    if (locRatioList == null) {
      return;
    }
    locRatioList.stream().forEach(parRatio -> System.out.println(parRatio));


    System.out.println("\n**** Quotation de l'actif 405 (2015-07-20 à 2015-07-22) ****");
    List<Price> locPriceList = locRESTManager.getQuotesList(405, "2015-07-20", "2015-07-22");
    if (locPriceList != null) {
      locPriceList.stream().forEach(parPrice -> System.out.println(parPrice));
    }

    System.out.println("\n**** Modification d'un portefeuille possédé ****");

    System.out.println("--- AVANT ---");
    Portfolio locPortfolio = locRESTManager.getPortfolio(RESTManager.ID_PTF_USER_1);
    if (locPriceList == null) {
      return;
    }
    System.out.println(locPortfolio);


    //Création d'une nouvelle composition
    List<DynAmountLineContainerModel> locLinesList = new ArrayList<>();
    DynAmountLineAssetModel locNewLine = new DynAmountLineAssetModel(325, 6.0);
    locLinesList.add(DynAmountLineContainerModel.of(locNewLine));
    Map<String, List<DynAmountLineContainerModel>> locNewCompo = new HashMap<>();
    locNewCompo.put("2012-02-01", locLinesList);
    locPortfolio._values = locNewCompo;
    //Push la nouvelle composition
    locRESTManager.pushPortfolio(RESTManager.ID_PTF_USER_1, locPortfolio);
    locPortfolio = locRESTManager.getPortfolio(RESTManager.ID_PTF_USER_1);
    if (locPortfolio == null) {
      return;
    }
    System.out.println("--- APRES ---");
    System.out.println(locPortfolio);

    System.out.println("\n**** Modification d'un portefeuille NON possédé ****");
    //Modification de la composition d'un portefeuille non possédé
    System.out.println("--- AVANT ---");
    Portfolio locPtfUser2 = locRESTManager.getPortfolio(RESTManager.ID_PTF_PRUDENT_USER2);
    if (locPtfUser2 == null) {
      return;
    }
    System.out.println(locPtfUser2);
    locPtfUser2._values = locNewCompo;
    locRESTManager.pushPortfolio(RESTManager.ID_PTF_PRUDENT_USER2, locPtfUser2);
    locPtfUser2 = locRESTManager.getPortfolio(RESTManager.ID_PTF_PRUDENT_USER2);
    System.out.println("--- APRES ---");
    System.out.println(locPtfUser2);

    //Test ratio
    //On récupère la liste des ratios sans benchmark
    System.out.println("\n**** Calcul de ratios ****");
    System.out.println("--- Liste des ratios ---");
    final List<Ratio> locRatioGood = locRatioList.stream()
            .filter(parRatio -> parRatio.is_benchmark_needed == false)
            .collect(Collectors.toList());
    locRatioGood.stream().forEach(parRatio -> System.out.println(parRatio));
    System.out.println("--- Résultat des ratios ---");
    Map<Integer, Map<Integer, JumpValue>> locRatioResult = locRESTManager.invokeRatios(
            locRatioGood,
            Arrays.asList(RESTManager.ID_PTF_USER_1),
            RESTManager.PERIOD_START_DATE,
            RESTManager.PERIOD_END_DATE);

    locRESTManager.printRatioResult(locRatioResult);

    locRESTManager.checkPotfolioConditions(
            RESTManager.ID_PTF_USER_1,
            new AssetCurrencyManager(RESTManager.PERIOD_START_DATE, locRESTManager));
  }
}
