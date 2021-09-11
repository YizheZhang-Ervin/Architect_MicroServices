package pers.ervin.product.controller;

import pers.ervin.product.entity.Product;
import pers.ervin.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RefreshScope //开启动态刷新
public class ProductController {

	@Autowired
	private ProductService productService;

	@Value("${name}")
	private String name;


	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public Product findById(@PathVariable Long id) {
		return productService.findById(id);
	}

	@RequestMapping(value = "/test")
	public String test() {
		return name;
	}
}
