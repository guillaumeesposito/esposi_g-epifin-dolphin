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

import javax.annotation.Nonnull;

/**
 * Représente un nombre monétaire (avec une devise)
 *
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 21/07/2017
 */
public final class MonetaryNumber {
  /**
   * La devise
   */
  String _currency;
  /**
   * Le montant
   */
  Double _amount;

  public MonetaryNumber(@Nonnull String parString) {
    String locCleanString = parString.replace(',','.');
    String delims = "[ ]";
    String[] tokens = locCleanString.split(delims);
    _amount = Double.parseDouble(tokens[0]);
    _currency = tokens[1];
  }
}