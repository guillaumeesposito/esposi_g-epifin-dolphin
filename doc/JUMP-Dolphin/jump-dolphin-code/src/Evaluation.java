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
 * daté du 21/07/2017.
 */

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Moulinette de vérification de tous les portefeuilles.
 *
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 21/07/2017
 */
public final class Evaluation {
  public static final int NB_USER = 10;
  public static final int ID_RETURN_ANNUAL = 17;
  public static final int ID_RETURN = 21;
  public static final int ID_SHARPE = 20;
  public static final int ID_VOL = 18;
  public static final int ID_EXPO = 29;
  public static Map<Integer,Ratio> RATIO_MAP = null;

  public static String USERTEST = "user_test";
  public static String PASSTEST = "jumpTest";
  private static int ID_PTF_REF = 595;
  /**
   * Liste des portefeuilles à évaluer. De préférence dans l'ordres des users
   */
  public static List<Integer> PORTFOLIO_ID_LIST = Arrays.asList(564, 567, 576, 570, 573, 574, 575, 572, 571, 577, ID_PTF_REF);
  public static void main(String[] args) throws IOException, URISyntaxException {

    final RESTManager locRESTManager = new RESTManager(
            RESTManager.HOST_NAME,
            RESTManager.PORT,
            USERTEST,
            PASSTEST);

    final AssetCurrencyManager locCurrencyManager = new AssetCurrencyManager(RESTManager.PERIOD_START_DATE, locRESTManager);

    final List<PortfolioEvaluationInfos> locPtfInfoList = new ArrayList(PORTFOLIO_ID_LIST.size());


    PORTFOLIO_ID_LIST.stream().forEach(parId -> {
      final Portfolio locPtf = locRESTManager.getPortfolio(parId);
      if (locPtf != null) {
        if (locRESTManager.checkPotfolioConditions(locPtf, locCurrencyManager)) {
          locPtfInfoList.add(new PortfolioEvaluationInfos(locPtf, true));
        } else {
          locPtfInfoList.add(new PortfolioEvaluationInfos(locPtf, false));
        }
      }
    });

    final List<Ratio> locRatioList = locRESTManager.getRatioList();
    final List<Ratio> locSharpeRatio = locRatioList.stream()
            .filter(parRatio -> parRatio.id == RESTManager.ID_SHARPE)
            .collect(Collectors.toList());
    if (locSharpeRatio == null || locSharpeRatio.size() == 0) {
      System.out.println("Pas de ratio de Sharpe. Vérifier l'identifiant du ratio. Sinon contacter JUMP.");
      return;
    }

    //Récupére les valeurs de sharpe de nos ptf
    Map<Integer, Map<Integer, JumpValue>> locRatioResult = locRESTManager.invokeRatios(
            locSharpeRatio,
            PORTFOLIO_ID_LIST,
            RESTManager.PERIOD_START_DATE,
            RESTManager.PERIOD_END_DATE);

    Iterator<PortfolioEvaluationInfos> locIterator = locPtfInfoList.iterator();

    Double locSharpeRef = null;

    while (locIterator.hasNext()) {
      PortfolioEvaluationInfos locPtfInfo = locIterator.next();
      final int locId = locPtfInfo._portfolio.id;
      final Map<Integer, JumpValue> locPtfSharpeResult = locRatioResult.get(locId);
      if (locPtfSharpeResult == null) {
        System.out.println(String.format("Pas de ratio de Sharpe pour le portefeuille d'ID %d", locId));
        locPtfInfo._isValid = false;
        locIterator.remove();
        continue;
      }
      final JumpValue locJumpValue = locPtfSharpeResult.get(RESTManager.ID_SHARPE);
      final Double locSharpeValue = (Double) locJumpValue.getRealValue();
      if (locSharpeValue == null) {
        System.out.println(String.format("Pas de ratio de Sharpe pour le portefeuille d'ID %d", locId));
        locPtfInfo._isValid = false;
        locIterator.remove();
        continue;
      }

      locPtfInfo._sharpe = locSharpeValue;

      if (locId == ID_PTF_REF) {
        locSharpeRef = locSharpeValue;
        locIterator.remove();
      }
    }

    //Trier les portefeuilles par meilleur ratios de sharpe dans l'ordre décroissant
    Collections.sort(locPtfInfoList, new Comparator<PortfolioEvaluationInfos>() {
      @Override
      public int compare(final PortfolioEvaluationInfos o1,
                         final PortfolioEvaluationInfos o2) {
        assert (o1._sharpe != null);
        assert (o2._sharpe != null);
        return -Double.compare(o1._sharpe, o2._sharpe); //ORDRE DECROISSANT
      }
    });

    final Double locFinalSharpeRef = locSharpeRef;

    List<PortfolioEvaluationInfos> locBetterPtf = new ArrayList<>();
    List<PortfolioEvaluationInfos> locNotBetterPtf = new ArrayList<>();
    List<PortfolioEvaluationInfos> locUnvalidPortfolioList = new ArrayList();

    locPtfInfoList.stream()
            .forEach(parPtfInfo -> {
              final boolean locIsBetter = locFinalSharpeRef == null
                      || (locFinalSharpeRef < parPtfInfo._sharpe && parPtfInfo._isValid);
              if (parPtfInfo._isValid == false) {
                locUnvalidPortfolioList.add(parPtfInfo);
                parPtfInfo._mark = 0;
              }else if (parPtfInfo._isValid && locIsBetter == false) {
                locNotBetterPtf.add(parPtfInfo);
                parPtfInfo._mark = 10;
              } else if (parPtfInfo._isValid && locIsBetter) {
                locBetterPtf.add(parPtfInfo);
              }
            });

    final int locPallierSize = locBetterPtf.size() / 10;

    for (int locI = 0; locI < locBetterPtf.size(); locI++) {
      final PortfolioEvaluationInfos locPtf = locBetterPtf.get(locI);
      locPtf._mark = 20 - ((locI - 1) / locPallierSize);
    }

    //Afficher les classements
    System.out.println(String.format("***CLASSEMENT - Sharpe référence %.5f***", locSharpeRef));
    for (int locI = 0; locI < locBetterPtf.size(); locI++) {
      final PortfolioEvaluationInfos locPtfInfo = locBetterPtf.get(locI);
      System.out.println(String.format("%d : %s / Sharpe : %.5f / Note : %d/20)",
              locI,
              locPtfInfo._portfolio._label,
              locPtfInfo._sharpe,
              locPtfInfo._mark));
    }
    System.out.println("***Hors classement mais valide (note = 10/20)***");
    for(PortfolioEvaluationInfos locPtfInfo : locNotBetterPtf) {
      System.out.println(String.format("%s / Sharpe : %.5f / Note : %d/20)",
              locPtfInfo._portfolio._label,
              locPtfInfo._sharpe,
              locPtfInfo._mark));
    }

    System.out.println("***Hors classement et ne sont pas valide (note = 0/20)***");
    for(PortfolioEvaluationInfos locPtfInfo : locUnvalidPortfolioList) {
      System.out.println(String.format("%s / Sharpe : %.5f / Note : %d/20)",
              locPtfInfo._portfolio._label,
              locPtfInfo._sharpe,
              locPtfInfo._mark));
    }


  }

  private static class PortfolioEvaluationInfos {
    Portfolio _portfolio = null;
    Double _sharpe = null;
    Integer _mark = null;
    boolean _isValid = false;

    public PortfolioEvaluationInfos(final Portfolio parPortfolio,
                                    final boolean parIsValid) {
      _portfolio = parPortfolio;
      _isValid = parIsValid;
    }

    public PortfolioEvaluationInfos(final Portfolio parPortfolio) {
      _portfolio = parPortfolio;
    }

    public PortfolioEvaluationInfos(final Portfolio parPortfolio,
                                    final Integer parMark) {
      _portfolio = parPortfolio;
      _mark = parMark;
    }
  }
}
