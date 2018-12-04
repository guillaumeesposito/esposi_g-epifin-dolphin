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
 * daté du 17/07/2017.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Container d'une ligne de portefeuille.
 * Qui peut être uniquement une ligne d'actif ou une ligne de liquidité.
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 17/07/2017
 */
public final class DynAmountLineContainerModel {
  /**
   * La ligne d'actif contenue.
   */
  @SerializedName("asset")
  public DynAmountLineAssetModel _asset;
  /**
   * La ligne de liquidité contenue.
   */
  @SerializedName("currency")
  public DynAmountLineCurrencyModel _currency;

  public DynAmountLineContainerModel() {
  }

  /**
   * Crée un container pour une ligne d'actif
   * @param parAssetLine La ligne d'actif à contenir
   * @return
   */
  public static DynAmountLineContainerModel of(final DynAmountLineAssetModel parAssetLine) {
    final DynAmountLineContainerModel locDynAmountLineContainerModel = new DynAmountLineContainerModel();
    locDynAmountLineContainerModel._asset = parAssetLine;
    return locDynAmountLineContainerModel;
  }

  /**
   * Crée un container pour une ligne de liquidité
   * @param parCurrencyLine la ligne de liquidité à contenir
   * @return
   */
  public static DynAmountLineContainerModel of(final DynAmountLineCurrencyModel parCurrencyLine) {
    final DynAmountLineContainerModel locDynAmountLineContainerModel = new DynAmountLineContainerModel();
    locDynAmountLineContainerModel._currency = parCurrencyLine;
    return locDynAmountLineContainerModel;
  }

  public String toString(){
    StringBuilder locString = new StringBuilder();
    if (_asset != null) {
      locString.append(_asset.toString());
    }
    if (_currency != null) {
      locString.append(_currency.toString());
    }
    return locString.toString();
  }
}
