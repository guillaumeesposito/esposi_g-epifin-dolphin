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
 * daté du 05/06/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Représente un portefeuille d'actifs
 * @author n.vic <nicolas.vic@jump-informatique.com>
 */
public class Portfolio {
  /**
   * Identifiant en base du portefeuille
   */
  public int id;
  /**
   * Nom du portefeuille
   */
  @SerializedName("label")
  public String _label;

  /**
   * Devise du portefeuille. 3 char code of currency
   */
  @SerializedName("currency")
  public AssetCurrency _currency;
  /**
   * Type du portefeuille (Forcément front)
   */
  @SerializedName("type")
  public EnumDynAmountTypeWebModel _type;
  /**
   * Ensemble des compositions du portefeuille.
   * Avec comme clefs la date de la composition et comme valeur une liste de lignes.
   */
  @SerializedName("values")
  public Map<String, List<DynAmountLineContainerModel>> _values;

  public Portfolio(){
  }

  public Portfolio(final String parLabel,
                   final AssetCurrency parCurrency,
                   final EnumDynAmountTypeWebModel parType,
                   final Map<String, List<DynAmountLineContainerModel>> parValues) {
    _label = parLabel;
    _currency = parCurrency;
    _type = parType;
    _values = parValues;
  }

  public String toString() {
    StringBuilder locString = new StringBuilder(String.format("%s\ncurrency : %s", _label, _currency));
    for (Entry<String, List<DynAmountLineContainerModel>> locEntry : _values.entrySet()) {
      locString.append("\n"+locEntry.getKey()+" : ");
      for (DynAmountLineContainerModel locLine : locEntry.getValue()) {
        locString.append(String.format("%s,", locLine.toString()));
      }
    }
    return locString.toString();
  }
}
