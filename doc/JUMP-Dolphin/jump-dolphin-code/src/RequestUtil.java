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

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * User: Paul PANGANIBAN <Paul.PANGANIBAN@jump-informatique.com>
 * Date: 17/07/2017
 */
public final class RequestUtil {

  public static HttpPost createPost(@Nonnull URIBuilder parURI, @Nonnull Object parEntity) throws URISyntaxException {
    HttpPost locHttpPost = new HttpPost(parURI.build());
    final String locS = new Gson().toJson(parEntity);
    locHttpPost.setEntity(new StringEntity(locS, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)));
    return locHttpPost;
  }

  public static HttpPut createPut(@Nonnull URIBuilder parURI, @Nonnull Object parEntity) throws URISyntaxException {
    HttpPut locHttpPut = new HttpPut(parURI.build());
    final String locS = new Gson().toJson(parEntity);
    locHttpPut.setEntity(new StringEntity(locS, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)));
    return locHttpPut;
  }
}
