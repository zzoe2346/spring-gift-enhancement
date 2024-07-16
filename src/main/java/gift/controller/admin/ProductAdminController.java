package gift.controller.admin;

import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductAdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductAdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/")
    public String getProducts(Model model, @PageableDefault(sort = "id") Pageable pageable) {
        model.addAttribute("products", productService.getProductResponses(pageable));
        return "version-SSR/index";
    }

    @GetMapping("/add")
    public String getAddForm(Model model) {
        model.addAttribute("addProductRequest", new AddProductRequest("", 0, "", 0L));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "version-SSR/add-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid AddProductRequest request) {
        try {
            productService.addProduct(request);
            return "redirect:/";
        } catch (Exception e) {
            return "version-SSR/add-error";
        }
    }

    @PostMapping("/deleteSelected")
    public String deleteSelectedProduct(@RequestParam("productIds") List<Long> productIds) {
        productService.deleteProducts(productIds);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("productId") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable("id") long id, Model model) {
        Product existingProduct = productService.getProduct(id);
        model.addAttribute("updateProductRequest", new UpdateProductRequest(existingProduct.getId(), existingProduct.getName(), existingProduct.getPrice(), existingProduct.getImageUrl(), existingProduct.getId()));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "version-SSR/edit-form";
    }

    @PostMapping("/edit")
    public String editProduct(@Valid UpdateProductRequest request) {
        try {
            productService.updateProduct(request);
            return "redirect:/";
        } catch (Exception e) {
            return "version-SSR/edit-error";
        }
    }

}
