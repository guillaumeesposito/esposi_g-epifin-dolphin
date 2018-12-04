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
 * Classe représentant un ratio
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 20/07/2017
 */
public final class Ratio {
  /**
   * Identifiant en base
   */
  int id;
  /**
   * Type de ratio
   */
  String type;
  /**
   * Nom du ratio
   */
  String name;
  /**
   * Définie si le ratio a besoin d'un benchmark
   */
  Boolean is_benchmark_needed;
  /**
   * Définie si le ratio retourne une valeur en pourcentage ou non
   */
  @SerializedName("is_percent")
  Boolean is_percent;

  @Override
  public String toString() {
    return id+" : "+name+" isPercent : "+is_percent;
  }
}