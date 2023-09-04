package com.tianji.data.controller;

import com.tianji.data.model.dto.BoardDataSetDTO;
import com.tianji.data.model.vo.EchartsVO;
import com.tianji.data.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BoardController
 * @Author wusongsong
 * @Date 2022/10/10 11:27
 * @Version
 **/
@RestController
@RequestMapping("/data/board")
@Api(tags = "看板数据相关操作")
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("")
    @ApiOperation("看板数据获取")
    public EchartsVO boardData(@RequestParam("types") List<Integer> types) {
        return boardService.boardData(types);
    }

    @PutMapping("set")
    @ApiOperation("看板数据设置")
    public void setBoardData(@Validated @RequestBody BoardDataSetDTO boardDataSetDTO) {
        boardService.setBoardData(boardDataSetDTO);
    }
}
