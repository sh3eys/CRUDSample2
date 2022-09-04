package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class TestController {

    private JdbcTemplate jdbcTemplate;

    //�R���X�g���N�^
    @Autowired
    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //�ꗗ��ʂ̕\��
    @GetMapping
    public String index(Model model) {
        String sql = "SELECT * FROM test_table";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        model.addAttribute("testList", list);
        return "sample/index";
    }

    //�V�K���̓t�H�[���̕\��
    @GetMapping("/form")
    public String form(@ModelAttribute TestForm testForm) {
        return "sample/form";
    }

    //�V�K���̓f�[�^�̕ۑ�
    @PostMapping("/form")
    public String create(TestForm testForm) {
        String sql = "INSERT INTO test_table(name, old) VALUES(?, ?);";
        jdbcTemplate.update(sql, testForm.getName(), testForm.getOld());
        return "redirect:/sample";
    }

    //�ҏW�t�H�[���̕\��
    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute TestForm testForm, @PathVariable int id) {
        String sql = "SELECT * FROM test_table WHERE id = " + id;
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        testForm.setId((int)map.get("id"));
        testForm.setName((String)map.get("name"));
        testForm.setOld((int)map.get("old"));
        return "sample/edit";
    }

    //�ҏW�f�[�^�̕ۑ�
    @PostMapping("/edit/{id}")
    public String update(TestForm testForm, @PathVariable int id) {
        String sql = "UPDATE test_table SET name = ?, old = ? WHERE id = " + id;
        jdbcTemplate.update(sql, testForm.getName(), testForm.getOld());
        return "redirect:/sample";
    }

    //�f�[�^�̍폜
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        String sql = "DELETE from test_table WHERE id = " + id;
        jdbcTemplate.update(sql);
        return "redirect:/sample";
    }
}