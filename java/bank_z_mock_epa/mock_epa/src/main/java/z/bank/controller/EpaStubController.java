package z.bank.controller;

@RestController
@RequestMapping("/api/epa")
public class EpaStubController {

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok().body(Map.of(
                "status", "OK",
                "message", "Token is valid"
        ));
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken() {
        String stubToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJhZG1pbiI6ZmFsc2UsImV4cCI6MTYxNDMzOTcwMH0.Zh-cFKuBktWwq-w-Tr1fgLAAlULAQOo866JryPijSIQ";
        return ResponseEntity.ok().body(Map.of(
                "token", stubToken
        ));
    }
}
