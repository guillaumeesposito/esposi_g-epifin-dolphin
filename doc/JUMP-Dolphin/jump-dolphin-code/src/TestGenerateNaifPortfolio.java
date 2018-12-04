import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Programme naif de référence.
 *
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public class TestGenerateNaifPortfolio {
    private static final int ID_SHARPE = 20;

    public static String LABEL_PTF = "PORTFOLIO_USER_REF";
    public static final int ID_MYPTF = 595;
    public static final String START_PERIOD = "2012-01-01";
    public static final String END_PERIOD = "2017-06-30";

    private static final int NB_LINES = 20;

    public static String USERTEST = "user_test";
    public static String PASSTEST = "jumpTest";

    public static void main(String[] args) throws IOException, URISyntaxException {
        RESTManager locRESTManager = new RESTManager(
                RESTManager.HOST_NAME,
                RESTManager.PORT,
                USERTEST,
                PASSTEST);

        //Récupère la liste de ratio
        Map<Integer,Ratio> locRatiosMap = locRESTManager.getRatioMap();

        //On récupère la liste des actions
        List<Asset> locAllAssetList = locRESTManager.getAssetList(START_PERIOD);
        List<Asset> locStocksList = locAllAssetList
                .stream()
                .filter(parAsset -> parAsset.assetType == EnumAssetType.STOCK)
                .collect(Collectors.toList());

        //On applique les ratios sur la liste
        List<Ratio> locRatiosToInvoke = new ArrayList<>();
        Ratio locSharpe = locRatiosMap.get(ID_SHARPE);

        locRatiosToInvoke.add(locSharpe);

        List<Integer> locIdAssetList = locStocksList.stream()
                .map(parAsset -> Integer.parseInt(parAsset.id._value))
                .collect(Collectors.toList());

        Map<Integer, Map<Integer, JumpValue>> locRatioInvoked = locRESTManager.invokeRatios(
                locRatiosToInvoke,
                locIdAssetList,
                START_PERIOD,
                END_PERIOD);

        List<RatioResult> locRatioResultList = new ArrayList<>();

        locRatioInvoked.forEach((s, stringNumberMap) ->
                locRatioResultList.add(new RatioResult(s, stringNumberMap)));

        System.out.println("\nCréation du portefeuille naif");
        //Création d'un portefeuille en fonction des volatillités
        Portfolio locSimpleSolution = createSimplePortfolio(locRESTManager, locRatioResultList, ID_SHARPE);
        locRESTManager.pushPortfolio(ID_MYPTF, locSimpleSolution);
        System.out.println(locRESTManager.getPortfolio(ID_MYPTF));
    }


    /**
     * Crée un portefeuille de NB_LINES constitué des actifs ayant les meilleurs
     * résultats pour le ratios parRatioToCompare
     * @param parRestManager REST Manager
     * @param parRatioResultList List des résulats de ratios
     * @param parRatioToCompare Le ratio sur lequel on se base
     */
    private static Portfolio createSimplePortfolio(
            RESTManager parRestManager,
            List<RatioResult> parRatioResultList,
            int parRatioToCompare) {
        DynAmountLineAssetModel locAssetLine;
        DynAmountLineContainerModel locContainerLine;
        List<DynAmountLineContainerModel> locListLines = new ArrayList<>(NB_LINES);
        Map<String, List<DynAmountLineContainerModel>> locCompo = new HashMap<>();
        AssetCurrency locPtfCurrency = new AssetCurrency();
        locPtfCurrency._code = "EUR";
        AssetCurrencyManager locCurrencyManager = new AssetCurrencyManager(START_PERIOD, parRestManager);
        double locQuantity;


        /*Construction du portefeuille risqué naif*/
        Collections.sort(parRatioResultList, new ComparatorByRatio(parRatioToCompare, false));

        int locI = 0;
        while (locListLines.size() < NB_LINES) {
            RatioResult ratioResult = parRatioResultList.get(locI);
            Integer assetID = ratioResult._assetID;
            Asset locAsset = parRestManager.getAsset(assetID, START_PERIOD);
            Map<String, Double> changeRateMap = locCurrencyManager.changeRateMap;
            MonetaryNumber locCloseValue = locAsset.monetaryPrice;
            locQuantity = 1000 / (locCloseValue._amount * changeRateMap.get(locCloseValue._currency));
            //if (locQuantity >= 0.5 && locQuantity <= 2) {
                locAssetLine = new DynAmountLineAssetModel(assetID, locQuantity);
                locContainerLine = new DynAmountLineContainerModel();
                locContainerLine._asset = locAssetLine;
                locListLines.add(locContainerLine);
            //}
            locI++;
        }

        locCompo.put(START_PERIOD, locListLines);

        return new Portfolio(
                LABEL_PTF,
                locPtfCurrency,
                EnumDynAmountTypeWebModel.front,
                locCompo);
    }

    private static class ComparatorByRatio implements Comparator<RatioResult> {
        private int _ratioID;
        private boolean _minOrder;

        private ComparatorByRatio(int parRatioID, boolean parMinOrder) {
            _ratioID = parRatioID;
            _minOrder = parMinOrder;
        }
        @Override
        public int compare(RatioResult r1, RatioResult r2) {
            final Object locRealValue1 = r1._ratioValue.get(_ratioID).getRealValue();
            double locValue1 = (double) locRealValue1;
            final Object locRealValue2 = r2._ratioValue.get(_ratioID).getRealValue();
            double locValue2 = (double) locRealValue2;

            if (locValue1 == locValue2) {
                return 0;
            }
            if (locValue2 - locValue1 > 0) {
                return _minOrder ? -1 : 1;
            }
            return _minOrder ? 1 : -1;
        }
    }

}
