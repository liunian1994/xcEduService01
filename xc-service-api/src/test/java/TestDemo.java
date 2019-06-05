import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.springframework.data.domain.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class TestDemo {

    @Test
    public void testByteCount(){
        System.out.println(Integer.toBinaryString(-4));
        int a = 3;// 0011
        int b = 6;// 0110
        System.out.println(a|b);//或 |
        System.out.println(a&b);//与 &
        System.out.println(a^b);//异或 ^
        System.out.println(~b);//非 ~ 取反
        System.out.println(~a);//非 ~ 取反
    }

    @Test
    public void test() throws UnsupportedEncodingException {
        String str = "·";
        int k = str.charAt(0);
        System.out.println((byte)k);
        byte[] t = str.getBytes();
        System.out.println(Arrays.toString(t));
        System.out.println(new String(t,"utf-8"));
    }

    @Test
    public void testChar(){
        System.out.println(2 & 31);
        System.out.println((byte)(0xe0 | 4));
        System.out.println( (13000>> 12));
        for (int i = 13000; i < 13100; i++) { //  2048 64-i/64  i%64 -128
            String str = (char)i + "";
            System.out.println(i + ":" + str + "  byte: " + Arrays.toString(str.getBytes()));
        }
        int x = 0x40;
        System.out.println(x);
        System.out.println( (183 >> 6 ) & x);
        String str =  "。；：‘？，（）·“《》、";
        for (int i = 0; i < str.length(); i++) {
            String s = str.charAt(i) + "";
            System.out.println( (int)str.charAt(i) + " : " +s + " : " + Arrays.toString(s.getBytes()));
        }

        System.out.println( str + "  byte: " + Arrays.toString(str.getBytes()));
    }

    //自定义条件查询测试
    @Test
    public void testFindAll() {
//条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());
//页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询
//ExampleMatcher.GenericPropertyMatchers.contains() 包含
//ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配
//条件值
        CmsPage cmsPage = new CmsPage();
//站点ID
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
//模板ID
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
// cmsPage.setPageAliase("分类导航");
//创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = new PageRequest(0, 10);
//        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
//        System.out.println(all);
    }


}
