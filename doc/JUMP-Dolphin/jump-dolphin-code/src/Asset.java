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

/**
 * Classe représentant un actif.
 *
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public final class Asset {

  /**
   * Identifiant en base de l'actif
   */
  @SerializedName("ASSET_DATABASE_ID")
  public JumpValue id;
  /**
   * Nom de l'acitf
   */
  @SerializedName("LABEL")
  public JumpValue label;
  /**
   * Dernière valeur de clôture
   */
  @SerializedName("LAST_CLOSE_VALUE_IN_CURR")
  public JumpValue priceValue;
  public MonetaryNumber monetaryPrice;

  /**
   * Type d'actifs
   */
  @SerializedName("TYPE")
  public JumpValue typeValue;
  public EnumAssetType assetType;

  @Override
  public String toString() {
    return id+" : "+label + " / "+priceValue;
  }
}

