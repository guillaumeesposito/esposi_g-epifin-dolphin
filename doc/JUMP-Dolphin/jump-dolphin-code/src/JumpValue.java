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
 * Valeur Jump.
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 17/07/2017
 */
public class JumpValue {

  /**
   * Type de la valeur
   */
  @SerializedName("type")
  public final String _type;
  /**
   * Valeur
   */
  @SerializedName("value")
  public final String _value;

  public JumpValue(final String parType,
                   final String parValue) {
    _type = parType;
    _value = parValue;
  }
  @Override
  public String toString() {
    return _value;
  }

  /**
   * Retourne une objet de _type avec valeur _value
   */
  public Object getRealValue(){
    return null;
  }
}
