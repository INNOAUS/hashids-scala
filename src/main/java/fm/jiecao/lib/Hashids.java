package fm.jiecao.lib;

import java.util.*;

/**
 * Hashids designed for Generating short hashes from numbers (like YouTube and Bitly), obfuscate
 * database IDs, use them as forgotten password hashes, invitation codes, store shard numbers
 * This is implementation of http://hashids.org v0.3.3 version.
 *
 * @author fanweixiao <fanweixiao@gmail.com>
 * @since 0.3.3
 */
public class Hashids {
  private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  private String salt = "";
  private String alphabet = "";
  private String seps = "cfhistuCFHISTU";
  private int minHashLength = 0;
  private double sepDiv = 3.5;
  private int guardDiv = 12;
  private int minAlphabetLength = 16;
  private String guards;

  public Hashids() throws Exception {
    this("");
  }

  public Hashids(String salt) throws Exception {
    this(salt, 0);
  }

  public Hashids(String salt, int minHashLength) throws Exception {
    this(salt, minHashLength, DEFAULT_ALPHABET);
  }

  public Hashids(String salt, int minHashLength, String alphabet) throws Exception {
    this.salt = salt;
    if(minHashLength < 0)
      this.minHashLength = 0;
    else
      this.minHashLength = minHashLength;
    this.alphabet = alphabet;

    String uniqueAlphabet = "";
    for(int i = 0; i < this.alphabet.length(); i++){
      if(!uniqueAlphabet.contains("" + this.alphabet.charAt(i))){
        uniqueAlphabet += "" + this.alphabet.charAt(i);
      }
    }

    this.alphabet = uniqueAlphabet;

    if(this.alphabet.length() < this.minAlphabetLength){
      throw new IllegalArgumentException("alphabet must contain at least " + this.minAlphabetLength + " unique characters");
    }

    if(this.alphabet.contains(" ")){
      throw new IllegalArgumentException("alphabet cannot contains spaces");
    }

    // seps should contain only characters present in alphabet;
    // alphabet should not contains seps
    for(int i = 0; i < this.seps.length(); i++){
      int j = this.alphabet.indexOf(this.seps.charAt(i));
      if(j == -1){
        this.seps = this.seps.substring(0, i) + " " + this.seps.substring(i + 1);
      } else {
        this.alphabet = this.alphabet.substring(0, j) + " " + this.alphabet.substring(j + 1);
      }
    }

    this.alphabet = this.alphabet.replaceAll("\\s+", "");
    this.seps = this.seps.replaceAll("\\s+", "");
    this.seps = this.consistentShuffle(this.seps, this.salt);

    if((this.seps.equals("")) || ((this.alphabet.length() / this.seps.length()) > this.sepDiv)){
      int seps_len = (int)Math.ceil(this.alphabet.length() / this.sepDiv);

      if(seps_len == 1){
        seps_len++;
      }

      if(seps_len > this.seps.length()){
        int diff = seps_len - this.seps.length();
        this.seps += this.alphabet.substring(0, diff);
        this.alphabet = this.alphabet.substring(diff);
      } else {
        this.seps = this.seps.substring(0, seps_len);
      }
    }

    System.out.println(this.seps);
    System.out.println(this.alphabet);

    this.alphabet = this.consistentShuffle(this.alphabet, this.salt);
    // use double to round up
    int guardCount = (int)Math.ceil((double)this.alphabet.length() / this.guardDiv);

    if(this.alphabet.length() < 3){
      this.guards = this.seps.substring(0, guardCount);
      this.seps = this.seps.substring(guardCount);
    } else {
      this.guards = this.alphabet.substring(0, guardCount);
      this.alphabet = this.alphabet.substring(guardCount);
    }

    System.out.println(this.seps);
    System.out.println(this.alphabet);
    System.out.println(this.guards);

  }

  /**
   * Encrypt numbers to string
   *
   * @param numbers the numbers to encrypt
   * @return the encrypt string
   */
  public String encrypt(long... numbers){
    String retval = "";
    if(numbers.length == 0) {
      return retval;
    }

    for(int j = 0; j < numbers.length; j++){
      System.out.println("+++" + numbers[j]);
    }

    return this.encode(numbers);
  }

  /**
   * Decrypt string to numbers
   *
   * @param hash the encrypt string
   * @return decryped numbers
   */
  public long[] decrypt(String hash){
    long[] ret = {};

    if(hash.equals(""))
      return ret;

    return this.decode(hash, this.alphabet);
  }

  private String encode(long... numbers){
    int numberHashInt = 0;
    for(int i = 0; i < numbers.length; i++){
      numberHashInt += (numbers[i] % (i+100));
      System.out.println("------>>>>");
      System.out.println(i);
      System.out.println(numbers[i]);
      System.out.println(numberHashInt);
      System.out.println("<<<<------");
    }
    String alphabet = this.alphabet;
    char ret = alphabet.toCharArray()[numberHashInt % alphabet.length()];
    char lottery = ret;
    long num;
    int sepsIndex, guardIndex;
    String buffer, ret_str = ret + "";
    char guard;

    System.out.println(">>" + numberHashInt);
    System.out.println(">>" + alphabet);
    System.out.println(">>" + alphabet.length());
    System.out.println(">>" + lottery);

    for(int i = 0; i < numbers.length; i++){
      num = numbers[i];
      buffer = lottery + this.salt + alphabet;

    System.out.println("==>" + lottery);
    System.out.println("==>" + this.salt);
    System.out.println("==>" + alphabet);
    System.out.println("==>" + buffer);

      alphabet = this.consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
      String last = this.hash(num, alphabet);


      ret_str += last;

    System.out.println("==>" + alphabet);
    System.out.println("==>" + last);
    System.out.println("==>" + ret_str);

      if(i + 1 < numbers.length){
        num %= ((int)last.toCharArray()[0] + i);
        sepsIndex = (int)(num % this.seps.length());
        ret_str += this.seps.toCharArray()[sepsIndex];
      }

    System.out.println("==>" + alphabet);
    System.out.println("==>" + ret_str);
    System.out.println("==>");
    }


    if(ret_str.length() < this.minHashLength){
      guardIndex = (numberHashInt + (int)(ret_str.toCharArray()[0])) % this.guards.length();
      guard = this.guards.toCharArray()[guardIndex];

      ret_str = guard + ret_str;

      if(ret_str.length() < this.minHashLength){
        guardIndex = (numberHashInt + (int)(ret_str.toCharArray()[2])) % this.guards.length();
        guard = this.guards.toCharArray()[guardIndex];

        ret_str += guard;
      }
    }

    int halfLen = alphabet.length() / 2;

    System.out.println("-->" + halfLen);
    System.out.println("-->" + alphabet);
    System.out.println("-->" + ret_str);

    while(ret_str.length() < this.minHashLength){
      alphabet = this.consistentShuffle(alphabet, alphabet);
      ret_str = alphabet.substring(halfLen) + ret_str + alphabet.substring(0, halfLen);
      int excess = ret_str.length() - this.minHashLength;
      if(excess > 0){
        int start_pos = excess / 2;
        ret_str = ret_str.substring(start_pos, start_pos + this.minHashLength);
      }
    }

    return ret_str;
  }

  private long[] decode(String hash, String alphabet){
    ArrayList<Long> ret = new ArrayList<Long>();

    int i = 0;
    String regexp = "[" + this.guards + "]";
    String hashBreakdown = hash.replaceAll(regexp, " ");
    String[] hashArray = hashBreakdown.split(" ");

    String op = "";
    for(String tmp : hashArray){
      op += tmp + ", ";
    }

    if(hashArray.length == 3 || hashArray.length == 2){
      i = 1;
    }

    hashBreakdown = hashArray[i];

    char lottery = hashBreakdown.toCharArray()[0];
    hashBreakdown = hashBreakdown.substring(1);
    hashBreakdown = hashBreakdown.replaceAll("[" + this.seps + "]", " ");
    hashArray = hashBreakdown.split(" ");

    String subHash = "", buffer = "";
    for(int j = 0; j < hashArray.length; j++){
      subHash = hashArray[j];
      buffer = lottery + this.salt + alphabet;
      alphabet = this.consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
      ret.add(this.unhash(subHash, alphabet));
    }

    //transform from List<Long> to long[]

    long[] arr = new long[ret.size()];
    for(int k = 0; k < arr.length; k++){
      arr[k] = ret.get(k);
    }

    return arr;
  }

  /* Private methods */
  private String consistentShuffle(String alphabet, String salt){
    if(salt.length() <= 0)
      return alphabet;

    char[] arr = salt.toCharArray();
    int asc_val, j;
    char tmp;
    for(int i = alphabet.length() - 1, v = 0, p = 0; i > 0; i--, v++){
      v %= salt.length();
      asc_val = (int)arr[v];
      p += asc_val;
      j = (asc_val + v + p) % i;

      tmp = alphabet.charAt(j);
      alphabet = alphabet.substring(0, j) + alphabet.charAt(i) + alphabet.substring(j + 1);
      alphabet = alphabet.substring(0, i) + tmp + alphabet.substring(i + 1);
    }

    return alphabet;
  }

  private String hash(long input, String alphabet){
    String hash = "";
    int alphabetLen = alphabet.length();
    char[] arr = alphabet.toCharArray();

    do {
      hash = arr[(int)(input % alphabetLen)] + hash;
      input /= alphabetLen;
    } while(input > 0);

    return hash;
  }

  private Long unhash(String input, String alphabet){
    long number = 0, pos;
    char[] input_arr = input.toCharArray();

    for(int i = 0; i < input.length(); i++){
      pos = alphabet.indexOf(input_arr[i]);
      number += pos * Math.pow(alphabet.length(), input.length() - i - 1);
    }

    return number;
  }

  public static int checkedCast(long value) {
    int result = (int) value;
    if (result != value) {
      // don't use checkArgument here, to avoid boxing
      throw new IllegalArgumentException("Out of range: " + value);
    }
    return result;
  }

  public String getVersion() {
    return "0.3.3";
  }
}
