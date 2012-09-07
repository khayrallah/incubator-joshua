package joshua.decoder.ff.tm;

import java.util.Arrays;
import java.util.Map;

import joshua.corpus.Vocabulary;

/**
 * Normally, the feature score in the rule should be *cost* (i.e., -LogP), so that the feature
 * weight should be positive
 * 
 * @author Zhifei Li, <zhifei.work@gmail.com>
 */
public class BilingualRule extends MonolingualRule {

	private String sparseFeatures;
  private int[] english;

  // ===============================================================
  // Constructors
  // ===============================================================

  /**
   * Constructs a new rule using the provided parameters. The owner and rule id for this rule are
   * undefined.
   * 
   * @param lhs Left-hand side of the rule.
   * @param sourceRhs Source language right-hand side of the rule.
   * @param targetRhs Target language right-hand side of the rule.
   * @param featureScores Feature value scores for the rule.
   * @param arity Number of nonterminals in the source language right-hand side.
   * @param owner
   * @param latticeCost
   * @param ruleID
   */
  public BilingualRule(int lhs, int[] sourceRhs, int[] targetRhs, float[] featureScores, int arity,
      int owner, float latticeCost, int ruleID) {
    super(lhs, sourceRhs, featureScores, arity, owner, latticeCost, ruleID);
    this.english = targetRhs;
  }

  // called by class who does not care about lattice_cost, rule_id, and owner
  public BilingualRule(int lhs, int[] sourceRhs, int[] targetRhs, float[] featureScores, int arity) {
    super(lhs, sourceRhs, featureScores, arity);
    this.english = targetRhs;
  }

	// Sparse feature version
  public BilingualRule(int lhs, int[] sourceRhs, int[] targetRhs, float[] denseFeatures, String sparseFeatures, int arity) {
    super(lhs, sourceRhs, denseFeatures, arity);
		this.sparseFeatures = sparseFeatures;
    this.english = targetRhs;
  }

  // ===============================================================
  // Attributes
  // ===============================================================

  public final void setEnglish(int[] eng) {
    this.english = eng;
  }

  public final int[] getEnglish() {
    return this.english;
  }

  // ===============================================================
  // Serialization Methods
  // ===============================================================
  // TODO: remove these methods

  // Caching this method significantly improves performance
  // We mark it transient because it is, though cf java.io.Serializable
  private transient String cachedToString = null;

  public String toString(Map<Integer, String> ntVocab) {
    if (null == this.cachedToString) {
      StringBuffer sb = new StringBuffer("[");
      sb.append(ntVocab.get(this.getLHS()));
      sb.append("] ||| ");
      sb.append(Vocabulary.getWords(this.getFrench()));
      sb.append(" ||| ");
      sb.append(Vocabulary.getWords(this.english));
      // sb.append(java.util.Arrays.toString(this.english));
      sb.append(" |||");
      for (int i = 0; i < this.getDenseFeatures().length; i++) {
        // sb.append(String.format(" %.12f", this.getFeatureScores()[i]));
        sb.append(' ');
        sb.append(Float.toString(this.getDenseFeatures()[i]));
      }
      this.cachedToString = sb.toString();
    }
    return this.cachedToString;
  }

  public String toString() {
    if (null == this.cachedToString) {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getEstCost() + " | ");
      sb.append(Vocabulary.word(this.getLHS()));
      sb.append(" ||| ");
      sb.append(Vocabulary.getWords(this.getFrench()));
      sb.append(" |||");
      for (int i = 0; i < english.length; i++) {
        if (english[i] < 0)
          sb.append(" ").append("NT" + english[i]);
        else
          sb.append(" ").append(Vocabulary.word(english[i]));
      }
      sb.append(" |||");
      for (int i = 0; i < this.getDenseFeatures().length; i++) {
        sb.append(String.format(" %.4f", this.getDenseFeatures()[i]));
      }
      this.cachedToString = sb.toString();
    }
    return this.cachedToString;
  }

  public String toStringWithoutFeatScores() {
    StringBuffer sb = new StringBuffer();
    sb.append(Vocabulary.word(this.getLHS()));
    return sb.append(" ||| ").append(convertToString(this.getFrench())).append(" ||| ")
        .append(convertToString(this.getEnglish())).toString();
  }

  /**
   * Two BilingualRules are equal of they have the same LHS, the same source RHS and the same target
   * RHS.
   * 
   * @param o the object to check for equality
   * @return true if o is the same BilingualRule as this rule, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof BilingualRule)) {
      return false;
    }
    BilingualRule other = (BilingualRule) o;
    if (getLHS() != other.getLHS()) {
      return false;
    }
    if (!Arrays.equals(getFrench(), other.getFrench())) {
      return false;
    }
    if (!Arrays.equals(english, other.getEnglish())) {
      return false;
    }
    return true;
  }

  public int hashCode() {
    // I just made this up. If two rules are equal they'll have the
    // same hashcode. Maybe someone else can do a better job though?
    int frHash = Arrays.hashCode(getFrench());
    int enHash = Arrays.hashCode(english);
    return frHash ^ enHash ^ getLHS();
  }

}
