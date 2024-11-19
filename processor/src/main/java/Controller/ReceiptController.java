package Controller;


import Model.Receipt;
import Service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;
    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processReceipt(@RequestBody Receipt receipt) {
        String receiptId = UUID.randomUUID().toString();
        Map<String, Object> pointsData = receiptService.calculatePoints(receipt);
        receiptService.saveReceipt(receiptId, pointsData);
        System.out.println("Current Receipts Dictionary: " + receiptService.getReceipts());
        return ResponseEntity.ok(Map.of("id", receiptId));
    }

    @GetMapping("/{receiptId}/points")
    public ResponseEntity<Object> getPoints(@PathVariable String receiptId) {
        Map<String, Object> pointsData = receiptService.getReceipt(receiptId);
        if (pointsData == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Receipt not found"));
        }
        return ResponseEntity.ok(pointsData);
    }
}
