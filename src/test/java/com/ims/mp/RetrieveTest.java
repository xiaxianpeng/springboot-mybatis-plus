package com.ims.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.ims.mp.entity.User;
import com.ims.mp.mapper.UserMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xianpeng.xia
 * on 2019-08-25 02:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RetrieveTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectById() {
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user);
    }


    @Test
    public void selectIds() {
        List<Long> idsList = Arrays.asList(1088248166370832385L, 1094592041087729666L, 1087982257332887553L);
        List<User> list = userMapper.selectBatchIds(idsList);
        list.forEach(System.err::println);
    }

    @Test
    public void selectByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "ts");
        columnMap.put("age", 26);
        List<User> list = userMapper.selectByMap(columnMap);
        list.forEach(System.err::println);
    }

    @Test
    public void selectByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //queryWrapper.like("name", "雨").lt("age", 40);
        //queryWrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");
        //queryWrapper.likeRight("name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
        //queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14").inSql("manager_id", "select id from user where name like '王%'");

        //name like '王%' and (age<40 or email is not null)
        //queryWrapper.likeRight("name", "王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));

        //name like '王%' or (age < 40 and age >20 and email is not null)//queryWrapper.likeRight("name", "王%").
        //    or(wq -> wq.lt("age", 40).gt("age", 20).isNotNull("email"));

        //(age<40 or email is not null ) and name like '王%'
        //queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email")).likeRight("name", "王%");

        //age in(30,31,34,35)
        //queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));

        //只返回满足条件的一条语句
        //queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");

        //queryWrapper.select("id", "name", "age").like("name", "雨").lt("age", 40);
        queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"))
            .like("name", "雨").lt("age", 40);
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.err::println);
    }

    @Test
    public void condition() {
        String name = "";
        String email = "y";
        QueryWrapper queryWrapper = new QueryWrapper<User>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
            .like(StringUtils.isNotEmpty(email), "email", email);
        List list = userMapper.selectList(queryWrapper);
        list.forEach(System.err::println);
    }

    @Test
    public void selectByWrapperEntity() {
        User whereUser = new User();
        whereUser.setName("大boss");
        whereUser.setAge(40);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(whereUser);
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.err::println);
    }


    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", 25);
        // false表示 值为null的不计入条件
        // params.put("age", null);
        // queryWrapper.allEq(params, false);

        // 第一个参数表示过滤条件
        //queryWrapper.allEq((k, v) -> !k.equals("name"), params);
        queryWrapper.allEq(params);
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.err::println);
    }

    @Test
    public void selectByWrapperMaps() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "age").like("name", "雨").lt("age", 40);
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.err::println);
    }

    @Test
    //分组查询　
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.select("avg(age) avg_age", "max(age) max_age", "min(age) min_age")
            .groupBy("manager_id")
            .having("sum(age) < {0}", 500);
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.err::println);
    }

    @Test
    public void selectByWrapperObjs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").like("name", "雨").lt("age", 40);
        List<Object> objects = userMapper.selectObjs(queryWrapper);
        objects.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨").lt("age", 40);
        Integer integer = userMapper.selectCount(queryWrapper);
        System.out.println("count: " + integer);
    }

    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "ts").lt("age", 40);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println("user: " + user);
    }

    @Test
    public void selectLambda() {
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.like(User::getName, "雨").lt(User::getAge, 40);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void selectLambda2() {
        List<User> userList = new LambdaQueryChainWrapper<User>(userMapper)
            .like(User::getName, "雨").ge(User::getAge, 20).list();
        userList.forEach(System.out::println);
    }

    @Test
    public void selectMy() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(User::getName, "王")
            .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> list = userMapper.selectAll(lambdaQueryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void selectByMySql() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(User::getName, "王")
            .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> list = userMapper.queryAll(lambdaQueryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void selectPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> userPage = new Page<>(1, 2, false);
       /* IPage<User> userIPage = userMapper.selectPage(userPage, queryWrapper);
        System.out.println("pages: " + userIPage.getPages());
        System.out.println("total: " + userIPage.getTotal());
        List<User> records = userIPage.getRecords();
        records.forEach(System.out::println);*/

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(userPage, queryWrapper);
        System.out.println("pages: " + mapIPage.getPages());
        System.out.println("total: " + mapIPage.getTotal());
        List<Map<String, Object>> records = mapIPage.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    public void selectMyPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> userPage = new Page<>(1, 2, true);
        IPage<User> mapIPage = userMapper.selectUserPage(userPage, queryWrapper);
        System.out.println("pages: " + mapIPage.getPages());
        System.out.println("total: " + mapIPage.getTotal());
        List<User> records = mapIPage.getRecords();
        records.forEach(System.out::println);
    }
}
