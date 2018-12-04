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
 * daté du 21/09/2017.
 */
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Paul PANGANIBAN <p.panganiban at jum-informatique.com>
 *         Date: 21/09/2017
 */
public final class NumberValue extends JumpValue {
  public NumberValue(final String parType,
                     final String parValue) {
    super(parType, parValue);
  }

  @Override
  public Object getRealValue() {
    Number locResult = null;
    NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);

    try {
      if (_type == null || _value == null) {
        return null;
      } else if ("percent".equals(_type)) {
        locResult = nf.parse(_value);
        locResult = locResult != null ? locResult.doubleValue() / 100 : null;
      } else {
        locResult = nf.parse(_value);
      }
    } catch (Exception parE) {
      System.out.println(_value);
      parE.printStackTrace();
    }
    return locResult;
  }
}
