import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * Paramètre d'un ratio
 */
public final class RatioMultiAssetParamModel {
  /**
   * Liste des id des ratios à calculer
   */
  @SerializedName("ratio")
  public List<Integer> _ratio;
  /**
   * Liste des actifs à calculer
   */
  @SerializedName("asset")
  public List<Integer> _assetIds;
  /**
   * Identifiant du benchmark (actif) à utiliser
   */
  @SerializedName("benchmark")
  public Integer _benchId;
  /**
   * Date de début de la période.
   * Pour la notation ce sera 2012-01-01 (yyyy-MM-dd)
   */
  @SerializedName("start_date")
  public String _startDate;
  /**
   * Date de début de la période.
   * Pour la notation ce sera 2017-06-30 (yyyy-MM-dd)
   */
  @SerializedName("end_date")
  public String _endDate;

  /**
   * Fréquence pour le calcul des ratios. Par défaut, une valeur null revient à Journalière.
   */
  @SerializedName("frequency")
  public String _frequency;

  public RatioMultiAssetParamModel(final List<Integer> parRatios,
                                   final List<Integer> parAssetIds,
                                   final Integer parBenchId,
                                   final String parStartDate,
                                   final String parEndDate,
                                   final String parFrequency) {
    _ratio = parRatios;
    _assetIds = parAssetIds;
    _benchId = parBenchId;
    _startDate = parStartDate;
    _endDate = parEndDate;
    _frequency = parFrequency;
  }

  @Override
  public boolean equals(final Object parOther) {
    if (this == parOther) {
      return true;
    }
    if ((parOther == null) || (getClass() != parOther.getClass())) {
      return false;
    }
    final RatioMultiAssetParamModel locThat = (RatioMultiAssetParamModel) parOther;
    return Objects.equals(_ratio, locThat._ratio) &&
            Objects.equals(_assetIds, locThat._assetIds) &&
            Objects.equals(_benchId, locThat._benchId) &&
            Objects.equals(_startDate, locThat._startDate) &&
            Objects.equals(_endDate, locThat._endDate) &&
            Objects.equals(_frequency, locThat._frequency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_ratio,
            _assetIds,
            _benchId,
            _startDate,
            _endDate,
            _frequency);
  }

  @Override
  public String toString() {
    return com.google.common.base.MoreObjects.toStringHelper(this)
            .add("_ratio", _ratio)
            .add("_asset", _assetIds)
            .add("_bench", _benchId)
            .add("_startDate", _startDate)
            .add("_endDate", _endDate)
            .add("_frequency", _frequency)
            .toString();
  }

}