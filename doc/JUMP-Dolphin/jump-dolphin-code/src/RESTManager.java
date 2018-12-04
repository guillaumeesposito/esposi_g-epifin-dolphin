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
 * daté du 11/07/2017.
 */

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * Classe pour gérer l'API REST JUMP
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 11/07/2017
 */
public class RESTManager {
  final static String HOST_NAME = "dolphin.jump-technology.com";
  final static int PORT = 3389;

  //final static String HOST_NAME = "172.22.75.200";
  //final static int PORT = 8080;
  //final static String HOST_NAME = "localhost";
  //final static int PORT = 14443;
  final static String SCHEME = "https";
  final static String URL = SCHEME + "://" + HOST_NAME + ":" + PORT + "/api/v1/";

  final static int ID_PTF_USER_1 = 564;
  final static int ID_PTF_BALANCED_USER1 = 565;
  final static int ID_PTF_RISKY_USER1 = 566;
  final static String USERNAME_USER1 = "epita_user_1";
  final static String PASSWORD_USER1 = "dolphin2017";

  final static int ID_PTF_PRUDENT_USER2 = 567;

  /**
   * Date de début de la période
   */
  public static String PERIOD_START_DATE = "2012-01-01";
  /**
   * Date de fin de la période
   */
  public static String PERIOD_END_DATE = "2017-06-30";
  /**
   * Nombre d'actifs du portefeuille
   */
  private static int NB_ASSETS = 20;
  /**
   * %NAV minimal pour un acitf du portefeuille
   */
  private static double MIN_NAV_PER_LINE = 0.01;
  /**
   * %NAV maximal pour un acitf du portefeuille
   */
  private static double MAX_NAV_PER_LINE = 0.1;



  public static final int ID_ANNUAL_RETURN = 17;
  public static final int ID_RETURN = 21;
  public static final int ID_SHARPE = 20;
  public static final int ID_VOL = 18;
  public static final int ID_EXPO = 29;

  /**
   * Client
   */
  private CloseableHttpClient _client;
  /**
   * Context client
   */
  private HttpClientContext _clientContext;

  public RESTManager(@Nonnull String parHostName,
                     final int parPort,
                     @Nonnull String parUsername,
                     @Nonnull String parPassword) {
    initClient(parHostName, parPort, parUsername, parPassword);
  }

  /**
   * Initialise _client et son _clientContext
   * @param parHostName HostName
   * @param parPort Port
   * @param parUsername Username
   * @param parPassword PassWord
   */
  public void initClient(@Nonnull String parHostName,
                         int parPort,
                         @Nonnull String parUsername,
                         @Nonnull String parPassword) {
    // Configure HttpClient to authenticate preemptively
    // by prepopulating the authentication data cache.

    // 1. Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();

    // 2. Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();
    final HttpHost locLocalhost = new HttpHost(parHostName, parPort, SCHEME);
    authCache.put(locLocalhost, basicAuth);

    // 3. Add AuthCache to the execution context
    _clientContext = HttpClientContext.create();
    _clientContext.setAuthCache(authCache);

    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
            new AuthScope(locLocalhost),
            new UsernamePasswordCredentials(parUsername, parPassword));
    SSLContextBuilder builder = new SSLContextBuilder();
    SSLConnectionSocketFactory sslsf = null;
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy(){
        @Override
        public boolean isTrusted(X509Certificate[] chain,
                                 String authType) throws CertificateException {
          return true;//FIXME : A MODIFIER QUAND HTTPS OK
        }
      });
      sslsf = new SSLConnectionSocketFactory(
              builder.build(), NoopHostnameVerifier.INSTANCE);//FIXME : A MODIFIER QUAND HTTPS OK
    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException parE) {
      parE.printStackTrace();
    }


    // 4. Create client executor and proxy
    _client = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(credsProvider)
            .setSSLSocketFactory(sslsf)
            .build();
  }

  /**
   * Retourne la liste complète des actifs.
   * Avec les valeurs des colonnes "ASSET_DATABASE_ID", "LABEL", "LABEL_CLOSE_VALUE_IN_CURR", "TYPE" à parDate.
   *
   * @param parDate
   * @return
   * @throws URISyntaxException
   * @throws IOException
   */
  public List<Asset> getAssetList(String parDate) {
    String locURI = URL + "asset";
    String locBody;
    URIBuilder locURIBuilder;
    HttpGet locHttpGet;

    Type locType;
    List<Asset> locAssets;

    try {
      locURIBuilder = new URIBuilder(locURI);
      locURIBuilder.addParameter("columns", "ASSET_DATABASE_ID");
      locURIBuilder.addParameter("columns", "LABEL");
      locURIBuilder.addParameter("columns", "LAST_CLOSE_VALUE_IN_CURR");
      locURIBuilder.addParameter("columns", "TYPE");
      locURIBuilder.addParameter("date", parDate);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locBody = executeRequest(_client, _clientContext, locHttpGet);
    } catch (URISyntaxException locE) {
      System.out.println("Erreur dans la syntaxe de l'URI :"+ locURI);
      return null;
    }

    if (locBody == null) {
      return null;
    }

    locType = TypeToken.getParameterized(List.class, Asset.class).getType();
    locAssets = (List<Asset>) convertJSONtoType(locBody, locType);
    if(locAssets == null) {
      return null;
    }

    locAssets.stream().forEach(parAsset -> {
      if (parAsset.priceValue != null) {
        parAsset.monetaryPrice = new MonetaryNumber(parAsset.priceValue._value);
      }
      parAsset.assetType = EnumAssetType.resolveAssetType(parAsset.typeValue._value);
    });

    return  locAssets;
  }

  /***
   * Permet de récupèrer l'actif parId avec des informations datées à parDate.
   * Les informations demandées sont (ASSET_DATABASE_ID, LABEL, LAST_CLOSE_VALUE_IN_CURR, TYPE)
   * @param parId Identifiant de l'actif
   * @param parDate Date des informations à récupérer
   * @return
   */
  public Asset getAsset(int parId, String parDate) {
    String locResponse = null;
    URIBuilder locURIBuilder;
    HttpGet locHttpGet = null;
    Asset locAsset = null;
    final String locURI = URL + "asset/" + parId;

    try {
      locURIBuilder = new URIBuilder(locURI);
      locURIBuilder.addParameter("columns", "ASSET_DATABASE_ID");
      locURIBuilder.addParameter("columns", "LABEL");
      locURIBuilder.addParameter("columns", "LAST_CLOSE_VALUE_IN_CURR");
      locURIBuilder.addParameter("columns", "TYPE");
      locURIBuilder.addParameter("date", parDate);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locResponse = executeRequest(_client, _clientContext, locHttpGet);
    } catch (URISyntaxException locE) {
      System.out.println("Erreur dans la syntaxe de l'URI :"+ locURI);
      return null;
    }

    if (locResponse == null) {
      return null;
    }

    final Type locType = TypeToken.getParameterized(Asset.class).getType();
    locAsset = (Asset) convertJSONtoType(locResponse, locType);
    if (locAsset == null) {
      return null;
    }

    locAsset.monetaryPrice = new MonetaryNumber(locAsset.priceValue._value);
    locAsset.assetType = EnumAssetType.resolveAssetType(locAsset.typeValue._value);

    return  locAsset;
  }

  /**
   * Récupère la liste des ratios disponibles
   */
  public List<Ratio> getRatioList() {
    URIBuilder locURIBuilder;
    String locBody;
    HttpGet locHttpGet;
    final String locURI = URL + "ratio";

    try {
      locURIBuilder = new URIBuilder(locURI);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locBody = executeRequest(_client, _clientContext, locHttpGet);
    } catch (URISyntaxException locE) {
      System.out.println("Erreur dans la syntaxe de l'URI :"+ locURI);
      return null;
    }

    if (locBody == null) {
      return null;
    }

    final Type locType = TypeToken.getParameterized(List.class, Ratio.class).getType();
    final List<Ratio> locRatioList = (List<Ratio>) convertJSONtoType(locBody, locType);
    return locRatioList;
  }

  /**
   * Récupère la liste des Ratios sur la base, sous forme de Map avec comme clefs l'identifiant du ratio et comme valeur
   * le ratio.
   */
  public Map<Integer, Ratio> getRatioMap() {
    final List<Ratio> locRatioList = getRatioList();
    if (locRatioList == null) {
      return null;
    }
    Map<Integer, Ratio> locRatioMap = new HashMap<>(locRatioList.size());
    locRatioList.stream().forEach(parRatio -> locRatioMap.put(parRatio.id, parRatio));
    return locRatioMap;
  }

  /**
   * Retourne le portefeuille parId
   */
  public Portfolio getPortfolio(final int parId) {
    String locResponse;
    URIBuilder locURIBuilder = null;
    HttpGet locHttpGet = null;
    final String locURL = URL + "portfolio/" + parId + "/dyn_amount_compo";
    try {
      locURIBuilder = new URIBuilder(locURL);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locResponse = executeRequest(_client, _clientContext, locHttpGet);
    } catch (URISyntaxException locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURL);
      return null;
    }
    if (locResponse == null) {
      return null;
    }
    final Type locType = TypeToken.getParameterized(Portfolio.class).getType();
    Portfolio locPortfolio = (Portfolio) convertJSONtoType(locResponse, locType);
    if (locPortfolio == null) {
      System.out.println("Le portefeuille "+parId+" n'a pas été trouvé.");
      return null;
    }

    locPortfolio.id = parId;
    return  locPortfolio;
  }

  /**
   * Remplace le portefeuille parIdPTF en base par parNewModel.
   *
   * @param parIdPTF
   * @param parNewModel
   */
  public boolean pushPortfolio(final int parIdPTF, final Portfolio parNewModel){
    //System.out.println(String.format("**** UPDATE PTF %d ****", parIdPTF));
    URIBuilder locURIBuilder;
    HttpPut locHttpPut;
    final String locURI = URL + "portfolio/" + parIdPTF + "/dyn_amount_compo";

    try {
      locURIBuilder = new URIBuilder(locURI);
      locHttpPut = RequestUtil.createPut(locURIBuilder, parNewModel);
    } catch (URISyntaxException locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURI);
      return false;
    }
    String locBody = executeRequest(_client, _clientContext, locHttpPut);
    if (locBody == null) {
      return false;
    }
    System.out.println("MAJ OK");
    return true;
  }

  /**
   * Retourne la dernière valeur de cloture à parDate en montant.
   * @param parId
   * @param parDate
   * @return
   */
  public MonetaryNumber getLastCloseValue(int parId, String parDate) {
    String locResponse = null;
    URIBuilder locURIBuilder;
    HttpGet locHttpGet = null;
    JumpValue locLastPrice = null;
    final String locURI = URL + "asset/" + parId + "/attribute/LAST_CLOSE_VALUE";
    try {
      locURIBuilder = new URIBuilder(locURI);
      locURIBuilder.addParameter("date", parDate);

      locHttpGet = new HttpGet(locURIBuilder.build());

      locResponse = executeRequest(_client, _clientContext, locHttpGet);

    } catch (URISyntaxException locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURI);
      return null;
    }

    if (locResponse == null) {
      return null;
    }
    final Type locType = TypeToken.getParameterized(JumpValue.class).getType();
    locLastPrice = (JumpValue) convertJSONtoType(locResponse, locType);
    if (locLastPrice == null) {
      return null;
    }
    return new MonetaryNumber(locLastPrice._value);
  }
  /**
   * Récupère la liste des cotations de l'actif parId dans la période entre parStartDate et parEndDate, les deux inclus.
   * Avec parStartDate <= parEnDate.
   *
   * @param parId Id de l'actif
   * @param parStartDate Début de la période (yyyy-MM-dd)
   * @param parEndDate Fin de la période (yyyy-MM-dd)
   * @return La liste de cotations
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public List<Price> getQuotesList(int parId,
                                   String parStartDate,
                                   String parEndDate) {
    String locResponse = null;
    URIBuilder locURIBuilder;
    HttpGet locHttpGet = null;
    List<Price> locPriceList = null;
    final String locURI = URL + "asset/" + parId + "/quote";
    try {

      locURIBuilder = new URIBuilder(locURI);
      locURIBuilder.addParameter("start_date", parStartDate);
      locURIBuilder.addParameter("end_date", parEndDate);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locResponse = executeRequest(_client, _clientContext, locHttpGet);
    } catch (URISyntaxException locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURI);
      return null;
    }
    if(locResponse == null) {
      return null;
    }

    final Type locType = TypeToken.getParameterized(List.class, Price.class).getType();
    locPriceList = (List<Price>) convertJSONtoType(locResponse, locType);
    return locPriceList;
  }

  /**
   * Effectue le calcul des ratios de parRatioList sur les actifs de parAssetsList dans la période parStartDate à
   * parEndDate). Dans notre cas pas de benchmark et une fréquence journalière.
   * @param parRatioList Liste des ratios à calculer
   * @param parAssetsList Liste des actifs sur lequel appliquer nos ratios
   * @param parStartDate Début de la période. Format : yyyy-MM-dd
   * @param parEndDate Fin de la période. Format : yyyy-MM-dd
   * @return Une map avec comme clefs l'identifiant de l'actif et comme valeur une map avec comme clefs l'identifiant du
   * ratio et comme valeur son résultat.
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public Map<Integer, Map<Integer, JumpValue>> invokeRatios(@Nonnull List<Ratio> parRatioList,
                           @Nonnull List<Integer> parAssetsList,
                           @Nonnull String parStartDate,
                           @Nonnull String parEndDate) {
    final RatioMultiAssetParamModel locParam = new RatioMultiAssetParamModel(
            parRatioList.stream().map(parRatio -> parRatio.id).collect(Collectors.toList()),
            parAssetsList,
            null, //Pas de benchmark par défaut
            parStartDate,
            parEndDate,
            null); //API REST par défaut DAILY

    final String locURI = URL + "ratio/invoke";

    URIBuilder locURIBuilder;
    HttpPost locHttpPost;
    String locResponse;

    try {
      locURIBuilder = new URIBuilder(locURI);
      locHttpPost = RequestUtil.createPost(locURIBuilder, locParam);
      locResponse = executeRequest(_client, _clientContext, locHttpPost);
    } catch (URISyntaxException locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURI);
      return null;
    }
    if (locResponse == null) {
      return null;
    }

    final Type locMapType = TypeToken.getParameterized(Map.class, Integer.class, NumberValue.class).getType();
    final Type locMap2Type = TypeToken.getParameterized(Map.class, Integer.class, locMapType).getType();
    Map<Integer, Map<Integer, JumpValue>> locResult = (Map<Integer, Map<Integer, JumpValue>>) convertJSONtoType(
            locResponse,
            locMap2Type);

    return locResult;
  }

  /**
   * Récupère le taux de chaanges de parCurrencySrc vers parCurrencyDest à parDate.
   * @param parDate Date cible
   * @param parCurrencySrc Devise à convertir
   * @param parCurrencyDest Devise cible
   * @return La valeur du taux de change
   */
  public Double getChangeRate(@Nonnull String parDate,
                              @Nonnull String parCurrencySrc,
                              @Nonnull String parCurrencyDest) {
    String locResponse = null;
    URIBuilder locURIBuilder;
    HttpGet locHttpGet;
    Double locChangeRate;
    final String locURI = String.format("%scurrency/rate/%s/to/%s",
            URL,
            parCurrencySrc,
            parCurrencyDest);
    try {

      locURIBuilder = new URIBuilder(locURI);
      locURIBuilder.addParameter("date",parDate);
      locHttpGet = new HttpGet(locURIBuilder.build());
      locResponse = executeRequest(_client, _clientContext, locHttpGet);
    } catch (Exception locE) {
      System.out.println("Vérifier la syntaxe de la requete "+locURI);
      return null;
    }

    if (locResponse == null) {
      return null;
    }

    locChangeRate = (Double) convertJSONtoType(locResponse, TypeToken.getParameterized(Double.class).getType());
    return locChangeRate;
  }

  /**
   * Affiche le résultat d'un ratio
   * @param parResultRatio
   */
  public void printRatioResult(@Nonnull Map<Integer, Map<Integer, JumpValue>> parResultRatio) {
    for(Entry<Integer, Map<Integer, JumpValue>> locEntry : parResultRatio.entrySet()) {
      System.out.println(locEntry.getKey());
      for (Entry<Integer, JumpValue> locRes : locEntry.getValue().entrySet()) {
        System.out.println("\t" + locRes.getKey() + " = " + locRes.getValue().getRealValue());
      }
    }
  }

  /**
   * Check si un portefeuille respecte les conditions du sujet.
   * Actuellement :
   * - Contient une unique composition
   * - La composition commence le 01/01/2012
   * - La composition contient exactement 20 actifs
   * - Le montant d'un actif doit être compris entre 1 et 10% du montant total du portefeuille.
   * @param parId id du portefeuille à vérifier.
   * @param parCurrencyManager Un currency manager
   * @return True si le protefeuille vérifie les conditions, false sinon.
   */
  public boolean checkPotfolioConditions(int parId, @Nonnull AssetCurrencyManager parCurrencyManager) {
    Portfolio locPortfolio = getPortfolio(parId);
    if (locPortfolio == null) {
      return false;
    }
    return checkPotfolioConditions(locPortfolio, parCurrencyManager);
  }
  /**
   * Check si un portefeuille respecte les conditions du sujet.
   * Actuellement : 
   * - Contient une unique composition
   * - La composition commence le 01/01/2012
   * - La composition contient exactement 20 actifs
   * - Le montant d'un actif doit être compris entre 1 et 10% du montant total du portefeuille.
   * @param parPortfolio Le portefeuille à check.
   * @param parCurrencyManager Un currency manager
   * @return True si le protefeuille vérifie les conditions, false sinon.
   */
  public boolean checkPotfolioConditions(@Nonnull Portfolio parPortfolio, @Nonnull AssetCurrencyManager parCurrencyManager) {

    System.out.print("Check du portefeuille "+parPortfolio._label+ " : ");

    final Map<String, List<DynAmountLineContainerModel>> locListCompo = parPortfolio._values;
    if (locListCompo == null || locListCompo.size() != 1) {
      System.out.println("n'a pas exactement UNE composition");
      return false;
    }
    final List<DynAmountLineContainerModel> locCompo = locListCompo.get(PERIOD_START_DATE);
    if (locCompo == null) {
      System.out.println(
              String.format("la composition ne date pas du %s", PERIOD_START_DATE));
      return false;
    }
    if (locCompo.size() != NB_ASSETS) {
      System.out.println(
              String.format("la composition à un nombre de lignes différent de %d", NB_ASSETS));
      return false;
    }

    //Clef : Nom de l'actif, Valeur : Montant dans le portefeuille (dans la devise du portefeuille).
    final Map<String, Double> locAssetAmountMap = new HashMap<>(20);

    //On check la proportion d'actifs du portefeuille
    double locTotalPtfAmount = 0;

    for (DynAmountLineContainerModel locLine : locCompo){
      final DynAmountLineCurrencyModel locLiquidity = locLine._currency;
      final DynAmountLineAssetModel locAssetLine = locLine._asset;
      if (locLiquidity == null && locAssetLine == null) {
        System.out.println("une ligne n'a ni liquidité ni actif");
        return false;
      }
      if (locLiquidity != null && locAssetLine != null) {
        //Si on ne sait pas si la ligne est une ligne de liquidité
        //ou une ligne d'actif le portefeuille est compté faux.
        System.out.println(
                String.format("une ligne possède une liquidité et un actif : %s",
                        locAssetLine));
        return false;
      }
      AssetCurrency locCurrency = null; //Currency de la ligne
      Double locLineAmount = null; //Montant de la ligne dans la devise de la ligne
      Double locAssetAmountInPtfCurrency = null; //Monant de la ligne dans la devise du portefeuille
      Double locTotalLineAmountInPTF = null; //Monant de la ligne dans la devise du portefeuille * quantité



      if (locLiquidity != null) {
        //Cas d'une liquidité
        locCurrency = locLiquidity._currency;
        locLineAmount = locLiquidity._amount;
        if (locCurrency != null && locLineAmount != null) {
          Double locChangeRate = parCurrencyManager.changeRateMap.get(locCurrency._code);
          assert (locChangeRate != null);
          locAssetAmountInPtfCurrency = locChangeRate * locLineAmount;
          locTotalPtfAmount += locAssetAmountInPtfCurrency;
          continue;
        }
        System.out.println(
                String.format("une ligne est incomplète : %s",
                        locAssetLine));
        return false;
      } else {
        //Sinon on requete le seveur pour avoir nos infos
        final int locID = locAssetLine._asset;
        Asset locAsset = getAsset(locID, PERIOD_START_DATE);

        if(locAsset.assetType == EnumAssetType.PORTFOLIO) {
          //Dans notre cas, un portefeuille ne peut pas en contenir un autre
          System.out.println(
                  String.format("contient un autre portefeuille (%s)", locAsset.label._value));
          return false;
        }

        final MonetaryNumber locPrice = locAsset.monetaryPrice;
        if (locPrice == null) {
          System.out.println(
                  String.format("ERREUR : pas de valeur de cloture pour %s",
                          locAsset.toString(),
                          PERIOD_START_DATE));
          return false;
        } else {
          Double locChangeRate = parCurrencyManager.changeRateMap.get(locPrice._currency);
          if (locChangeRate == null) {
            System.out.println(
                    String.format("ERREUR : pas de taux de change %s -> %s à %s",
                            locAsset.monetaryPrice._currency,
                            parPortfolio._currency._code,
                            PERIOD_START_DATE));
            return false;
          }
          locAssetAmountInPtfCurrency = locPrice._amount * locChangeRate;
          locTotalLineAmountInPTF = locAssetAmountInPtfCurrency * locAssetLine._quantity;

          if (locAssetAmountMap.containsKey(locAsset.label._value)) {
            //Dans notre cas, un portefeuille ne peut pas en contenir un autre
            System.out.println(
                    String.format("contient deux fois l'actif ", locAsset.label._value));
            return false;
          }

          locAssetAmountMap.put(locAsset.label._value, locTotalLineAmountInPTF);
          locTotalPtfAmount += locTotalLineAmountInPTF;
        }
      }
    }
    final double locFinalTotalPtfAmount = locTotalPtfAmount;
    final List<Entry<String, Double>> locUnvalidLines = locAssetAmountMap.entrySet().stream()
            .filter(parEntry -> {
              final double locWeight = parEntry.getValue() / locFinalTotalPtfAmount;
              final boolean locIsNotValidWeight = locWeight < MIN_NAV_PER_LINE || locWeight > MAX_NAV_PER_LINE;
              if (locIsNotValidWeight == true) {
                System.out.println(String.format("la ligne d'actif %s a un %NAV de  %.5f", parEntry.getKey(), locWeight));
              }
              return locIsNotValidWeight;})
            .collect(Collectors.toList());

    if (locUnvalidLines.size() > 0) {
      return false;
    }
    System.out.println("OK");
    return true;
  }


  /**
   * Exécute une requete via un cilent dans un certain contexte.
   *
   * @param parHttpClient
   * @param parClientContext
   * @param parRequest
   * @return
   */
  private String executeRequest(@Nonnull final CloseableHttpClient parHttpClient,
                               @Nonnull final HttpClientContext parClientContext,
                               @Nonnull final HttpRequestBase parRequest) {
    HttpResponse locExecute;
    HttpEntity locEntity;
    String locResponse;
    InputStreamReader locInputStreamReader;
    Integer locStatusCode;
    try {
      locExecute = parHttpClient.execute(parRequest, parClientContext);
      locStatusCode = locExecute.getStatusLine().getStatusCode();
      locEntity = locExecute.getEntity();
      locInputStreamReader = new InputStreamReader(locEntity.getContent(), StandardCharsets.UTF_8);
      locResponse = CharStreams.toString(locInputStreamReader);
      locInputStreamReader.close();
    } catch (IOException locE) {
      System.out.println("Erreur lors de l'execution de la requete "+parRequest.toString()
              +" vérifier si votre connexion internet et si le serveur est fonctionnel.");
      return null;
    }

    switch (locStatusCode.intValue()) {
      case 401 :
        System.out.println("Probleme d'authentification");
        return null;
      case 403 :
        System.out.println("Acces refuse");
        return null;
      case 404 :
        System.out.println("Page non trouvee");
        return null;
      case 500 :
      case 503 :
        System.out.println("Probleme sur le serveur :\n"+ locResponse);
        return null;
    }
    return locResponse;
  }

  private Object convertJSONtoType(String parString, Type parType) {
    Object locO = null;
    try {
      locO = new Gson().fromJson(parString, parType);
    } catch (Exception locE){
      System.out.print("Erreur lors de la conversion en "+ parType.getTypeName() + " de l'objet "+parString);
    }
    return locO;
  }
}
