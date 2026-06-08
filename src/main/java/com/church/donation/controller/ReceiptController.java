package com.church.donation.controller;

import com.church.donation.dto.ReceiptResponse;
import com.church.donation.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // 💡 PathVariable 대신 RequestParam 사용!

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    // 💡 [완벽 매핑] 주소창의 /receipt?memberId=M9999&year=2026 을 그대로 낚아챕니다.
    @GetMapping("/receipt")
    public String showReceipt(@RequestParam String memberId,
                              @RequestParam(required = false, defaultValue = "2026") String year,
                              Model model) {

        // 💡 [연도 파싱] String으로 들어온 연도를 int 타입으로 변환합니다.
        int intYear = 2026;
        try {
            intYear = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            // 혹시라도 숫자가 아닌 값이 들어오면 기본값 2026 유지
        }

        // 💡 [서비스 호출] 수정된 서비스 양식에 맞게 memberId와 intYear를 함께 전달합니다!
        ReceiptResponse data = receiptService.generateReceiptData(memberId, intYear);

        // 💡 HTML이 ${church}로 찾으므로 이름표 세팅
        model.addAttribute("church", data.getChurchInfo());

        // 💡 HTML이 ${data.name}, ${data.totalAmount} 등으로 찾으므로 하나의 Map으로 묶어 전달
        Map<String, Object> htmlData = new HashMap<>();
        htmlData.put("name", data.getMember().getName());
        htmlData.put("birthDate", data.getMember().getBirthDate());
        htmlData.put("totalAmount", data.getTotalAmount());

        htmlData.put("itemCode", "41");
        htmlData.put("dateRange", year + ". 1~12");

        model.addAttribute("data", htmlData);

        // src/main/resources/templates/receipt.html 화면을 띄웁니다.
        return "receipt";
    }
}