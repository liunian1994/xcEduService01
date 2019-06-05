package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);

    }

    //分页查询
    @Test
    public void testFindPage(){
        //分页参数
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //修改
    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5b4b1d8bf73c6623b03f8cec");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //设置要修改值
            cmsPage.setPageAliase("test01");
            //...
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }

    //根据页面名称查询
    @Test
    public void testfindByPageName(){
        CmsPage cmsPage = cmsPageRepository.findByPageName("测试页面");
        System.out.println(cmsPage);
    }

    //自定义条件查询测试
    @Test
    public void testFindAll1() {
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
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());
        System.out.println("--------------1------------");
    }

    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity =
                restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f",
                        Map.class);
        System.out.println(forEntity);
    }

    //GridFS存文件
    @Test
    public void testGridFs() throws FileNotFoundException {
        //要存储的文件
        File file = new File("d:/index_banner.ftl");
        //定义输入流
        FileInputStream inputStram = new FileInputStream(file);
        //向GridFS存储文件
        ObjectId objectId = gridFsTemplate.store(inputStram, "轮播图01", "");
        //得到文件ID
        String fileId = objectId.toString();
        System.out.println(file);
    }


    //GridFS读文件
    @Test
    public void queryFile() throws IOException {
        String fileId = "5aec5d8c0e6618376c08e47d";
        //根据id查询文件
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(s);
    }

    //删除文件
    @Test
    public void testDelFile() throws IOException {
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5caf357180c062da30995f79")));
    }

    //文件存储2  5cbde4e2dfe91317dce655a0  5cbe8848071ded88dcc9501c
    @Test
    public void testStore2() throws FileNotFoundException {
        File file = new File("D:\\course.ftl");
        FileInputStream inputStream = new FileInputStream(file);
        //保存模版文件内容  5aec5d8c0e6618376c08e47d 之前
        ObjectId gridFSFile = gridFsTemplate.store(inputStream, "课程详情模板文件","");
        String fileId = gridFSFile.toString();
        System.out.println(fileId);
    }

}
