package fm.jiecao.lib;

import java.util.Arrays;

import org.junit.Test;
import junit.framework.Assert;

import fm.jiecao.lib.Hashids;

public class HashidsTest {

	@Test
	public void test_issue11() throws Exception
	{
    final Hashids hashIds = new Hashids();
    //final long id = 3945448269169105809L;
    long id = 9007199254740992L;
		String id_str = "9007199254740992 (2^53)";
		String str = hashIds.encrypt(id);
		System.out.println(id);
		System.out.println(id_str);
		System.out.println(str);
		System.out.println("---");

		id = 9007199254740993L;
		id_str = "9007199254740993 (2^53+1)";
		str = hashIds.encrypt(id);
		System.out.println(id);
		System.out.println(id_str);
		System.out.println(str);
		System.out.println("---");

		id = 18014398509481984L;
		id_str = "18014398509481984 (2^54)";
		str = hashIds.encrypt(id);
		System.out.println(id);
		System.out.println(id_str);
		System.out.println(str);
		System.out.println("---");

		id = 18014398509481985L;
		id_str = "18014398509481985 (2^54+1)";
		str = hashIds.encrypt(id);
		System.out.println(id);
		System.out.println(id_str);
		System.out.println(str);
		System.out.println("---");

		id = 1152921504606846976L;
		id_str = "1152921504606846976 (2^60)";
		str = hashIds.encrypt(id);
		System.out.println(id);
		System.out.println(id_str);
		System.out.println(str);
		System.out.println("---");
    ///Assert.assertEquals(id, hashIds.decrypt(str)[0]);
	}
//
// 	@Test
// 	public void test_one_number(){
//     Hashids a = null;
//     String expected = "NkK9", res= "";
//     long num_to_hash = 12345L;
//     long[] res2;
//     try {
//       a = new Hashids("this is my salt");
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     res = a.encrypt(num_to_hash);
//     Assert.assertEquals(res, expected);
//     res2 = a.decrypt(expected);
//     Assert.assertEquals(res2.length, 1);
//     Assert.assertEquals(res2[0], num_to_hash);
// 	}
//
// 	@Test
// 	public void test_serveral_numbers(){
//     Hashids a = null;
//     String expected = "aBMswoO2UB3Sj", res= "";
//     long[] num_to_hash = {683L, 94108L, 123L, 5L}, res2;
//     try {
//       a = new Hashids("this is my salt");
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     res = a.encrypt(num_to_hash);
//     Assert.assertEquals(res, expected);
//     res2 = a.decrypt(expected);
//     Assert.assertEquals(res2.length, num_to_hash.length);
//     Assert.assertTrue(Arrays.equals(res2, num_to_hash));
// 	}
//
//   @Test
//   public void test_specifying_custom_hash_length(){
//     Hashids a = null;
//     String expected = "gB0NV05e", res= "";
//     long num_to_hash = 1L;
//     long[] res2;
//     try {
//       a = new Hashids("this is my salt", 8);
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     res = a.encrypt(num_to_hash);
//     Assert.assertEquals(res, expected);
//     res2 = a.decrypt(expected);
//     Assert.assertEquals(res2.length, 1);
//     Assert.assertEquals(res2[0], num_to_hash);
//   }
//
//   @Test
//   public void test_randomness(){
//   Hashids a = null;
//   String expected = "1Wc8cwcE", res= "";
//   long[] num_to_hash = {5L, 5L, 5L, 5L}, res2;
//   try {
//     a = new Hashids("this is my salt");
//   } catch (Exception e) {
//     e.printStackTrace();
//   }
//   res = a.encrypt(num_to_hash);
//   Assert.assertEquals(res, expected);
//   res2 = a.decrypt(expected);
//   Assert.assertEquals(res2.length, num_to_hash.length);
//   Assert.assertTrue(Arrays.equals(res2, num_to_hash));
//   }
//
//   @Test
//   public void test_randomness_for_incrementing_numbers(){
//   Hashids a = null;
//   String expected = "kRHnurhptKcjIDTWC3sx", res= "";
//   long[] num_to_hash = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L}, res2;
//   try {
//     a = new Hashids("this is my salt");
//   } catch (Exception e) {
//     e.printStackTrace();
//   }
//   res = a.encrypt(num_to_hash);
//   Assert.assertEquals(res, expected);
//   res2 = a.decrypt(expected);
//   Assert.assertEquals(res2.length, num_to_hash.length);
//   Assert.assertTrue(Arrays.equals(res2, num_to_hash));
//   }
//
//   @Test
//   public void test_randomness_for_incrementing(){
//   Hashids a = null;
//   try {
//     a = new Hashids("this is my salt");
//   } catch (Exception e) {
//     e.printStackTrace();
//   }
//   Assert.assertEquals(a.encrypt(1L), "NV");
//   Assert.assertEquals(a.encrypt(2L), "6m");
//   Assert.assertEquals(a.encrypt(3L), "yD");
//   Assert.assertEquals(a.encrypt(4L), "2l");
//   Assert.assertEquals(a.encrypt(5L), "rD");
//   }
//
//   @Test
//   public void test_for_vlues_greater_int_maxval(){
//   Hashids a = null;
//   try {
//     a = new Hashids("this is my salt");
//   } catch (Exception e) {
//     e.printStackTrace();
//   }
//   Assert.assertEquals(a.encrypt(9876543210123L), "Y8r7W1kNN");
// }
//
// 	@Test
// 	public void test_issue10(){
// 		Hashids a = null;
// 		String expected = "3kK3nNOe", res= "";
// 		long num_to_hash = 75527867232l;
// 		long[] res2;
// 		try {
// 			a = new Hashids("this is my salt");
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
// 		res = a.encrypt(num_to_hash);
// 		Assert.assertEquals(res, expected);
// 		res2 = a.decrypt(expected);
// 		Assert.assertEquals(res2.length, 1);
// 		Assert.assertEquals(res2[0], num_to_hash);
// 	}
}
