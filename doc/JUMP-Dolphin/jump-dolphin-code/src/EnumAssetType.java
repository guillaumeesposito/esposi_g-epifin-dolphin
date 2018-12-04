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

/**
 * Enum des différents types d'actifs disponnibles
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 21/07/2017
 */
public enum EnumAssetType {
  STOCK,
  FUND,
  PORTFOLIO,
  BOND;

  EnumAssetType() {
  }

  public static EnumAssetType resolveAssetType(String parString) {
    if ("STOCK".equals(parString))
      return EnumAssetType.STOCK;
    if ("FUND".equals(parString))
      return EnumAssetType.FUND;
    if ("PORTFOLIO".equals(parString))
      return EnumAssetType.PORTFOLIO;
    if ("BOND".equals(parString))
      return EnumAssetType.BOND;

    return null;
  }
}
