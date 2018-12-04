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

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public class Price {
  /**
   * Date des valeurs
   */
  public Date date;
  /**
   * NAV
   */
  @SerializedName("nav")
  public double _nav;
  /**
   * Montant brut
   */
  @SerializedName("gross")
  public double _gross;
  /**
   * +/- value
   */
  @SerializedName("pl")
  public double _pl;
  /**
   * Valeur de clotûre
   */
  @SerializedName("close")
  public double _close;
  /**
   * Rendement
   */
  @SerializedName("return")
  public double _return;

  @Override
  public String toString() {
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
    return dt.format(date)+" : "+_close;
  }
}