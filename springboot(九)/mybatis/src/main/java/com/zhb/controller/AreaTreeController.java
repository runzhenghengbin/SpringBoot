package com.zhb.controller;

import com.zhb.entity.AreaTreeEntity;
import com.zhb.mapper.AreaTreeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: curry
 * @Date: 2018/6/8 22:50
 * @Description:
 */
@RestController
public class AreaTreeController {

    @Resource
    private AreaTreeMapper areaTreeMapper;


    @GetMapping(value = "/areaTree")
    public List<AreaTreeEntity> getTree(){
        return areaTreeMapper.getAreaTree();

    }
    @GetMapping(value = "/area")
    public   List<AreaTreeEntity> get(){
        List<AreaTreeEntity> s =  new ArrayList<>();

        List<AreaTreeEntity> res= new ArrayList<>();
        Map<String,List<AreaTreeEntity>> childMap = new HashMap<>(16);

        AreaTreeEntity a = new AreaTreeEntity();
        a.setId("1");
        a.setName("山东");
        a.setPid("0");
        s.add(a);

        AreaTreeEntity a1 = new AreaTreeEntity();
        a1.setId("2");
        a1.setName("济南");
        a1.setPid("1");
        s.add(a1);

        AreaTreeEntity a2 = new AreaTreeEntity();
        a2.setId("3");
        a2.setName("高新区");
        a2.setPid("2");
        s.add(a2);

        for(AreaTreeEntity entity : s){

            if ("0".equals(entity.getPid())) {

                res.add(entity);
            }
            else {

                List<AreaTreeEntity> childList = (childMap.containsKey(entity.getPid())) ? childMap.get(entity.getPid()) : new ArrayList<>();
                childList.add(entity);

                if (!childMap.containsKey(entity.getPid())){
                    childMap.put(entity.getPid(),childList);
                }
            }
        }

        for(AreaTreeEntity entity : res){
            findChild(entity,childMap);
        }

        return res;
    }

    public void findChild(AreaTreeEntity entity,Map<String,List<AreaTreeEntity>> childMap){

        if (childMap.containsKey(entity.getId())){
            List<AreaTreeEntity> chidList = childMap.get(entity.getId());
            for (AreaTreeEntity childEntity : chidList){
                findChild(childEntity,childMap);
            }
            entity.setChildren(chidList);
        }
    }
}

