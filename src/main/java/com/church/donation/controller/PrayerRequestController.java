package com.church.donation.controller;

import com.church.donation.domain.PrayerRequest;
import com.church.donation.service.PrayerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller // 화면(HTML)과 API(JSON)를 둘 다 주기 위해 @Controller 사용
@RequiredArgsConstructor
public class PrayerRequestController {

    private final PrayerRequestService prayerService;

    // 1. [화면 열기] 주소창에 /prayer-board 치면 HTML 띄워주기
    @GetMapping("/prayer-board")
    public String showPrayerBoard() {
        return "prayer_board"; // 👈 아까 만든 HTML 파일 이름
    }

    // 2. [API] 월별 리스트 주기 (JSON)
    @ResponseBody
    @GetMapping("/api/prayers")
    public ResponseEntity<List<PrayerRequest>> getPrayers(@RequestParam String month) {
        return ResponseEntity.ok(prayerService.getPrayersByMonth(month));
    }

    // 3. [API] 새 기도제목 저장하기
    @ResponseBody
    @PostMapping("/api/prayers")
    public ResponseEntity<String> createPrayer(@RequestBody PrayerRequest request) {
        prayerService.createPrayer(request);
        return ResponseEntity.ok("success");
    }

    // 4. [API] 삭제하기 (권한 확인 포함)
    @ResponseBody
    @PostMapping("/api/prayers/{id}/delete")
    public ResponseEntity<?> deletePrayer(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            // 프론트에서 보낸 JSON {"password": "1234"} 안에서 비밀번호 꺼내기
            String inputPassword = payload.get("password");

            // 💡 현재 로그인한 사람이 '관리자(Admin)'인지 확인하는 스프링 시큐리티 로직!
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN") || a.getAuthority().equals("ROLE_ADMIN"));

            // 서비스로 넘겨서 삭제 시도
            prayerService.deletePrayer(id, inputPassword, isAdmin);

            return ResponseEntity.ok("deleted");

        } catch (IllegalArgumentException e) {
            // 비밀번호가 틀리면 400 에러와 함께 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}