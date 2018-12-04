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
 * Ligne de liquidité de portefeuille
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 17/07/2017
 */
public final class DynAmountLineCurrencyModel {
  /**
   * Devise de la liquidité.
   */
  @SerializedName("currency")
  public AssetCurrency _currency;
  /**
   * Montant de la liquidité.
   */
  @SerializedName("amount")
  public Double _amount;

  public DynAmountLineCurrencyModel(final AssetCurrency parCurrency,
                                    final Double parAmount) {
    _currency = parCurrency;
    _amount = parAmount;
  }

  public String toString(){
    return "(Amount :"+_amount+", "+_currency.toString()+")";
  }
}
