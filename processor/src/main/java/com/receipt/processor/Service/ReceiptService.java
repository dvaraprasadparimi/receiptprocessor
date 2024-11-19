package com.receipt.processor.Service;

import com.receipt.processor.Model.Receipt;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiptService {

    private final Map<String, Map<String, Object>> receipts = new HashMap<>();

    public Map<String, Object> calculatePoints(Receipt receipt) {
        int points = 0;
        List<String> breakdown = new ArrayList<>();

        // Rule: One point for each alphanumeric character in retailer name
        String retailer = receipt.getRetailer();
        int retailerPoints = (int) retailer.chars().filter(Character::isLetterOrDigit).count();
        points += retailerPoints;
        breakdown.add(retailerPoints + " points - retailer name (" + retailer + ") has " + retailerPoints + " alphanumeric characters");

        // Rule: 50 points if total is a round dollar amount
        double total = receipt.getTotal();
        if (total == Math.floor(total)) {
            points += 50;
            breakdown.add("50 points - total is a round dollar amount");
        }

        // Rule: 25 points if total is a multiple of 0.25
        if (total % 0.25 == 0) {
            points += 25;
            breakdown.add("25 points - total is a multiple of 0.25");
        }

        // Rule: 5 points for every two items
        List<Receipt.Item> items = receipt.getItems();
        int itemPairsPoints = (items.size() / 2) * 5;
        points += itemPairsPoints;
        breakdown.add(itemPairsPoints + " points - " + items.size() + " items (" + items.size() / 2 + " pairs @ 5 points each)");

        // Rule: 0.2 * price for items with description length multiple of 3
        for (Receipt.Item item : items) {
            String description = item.getShortDescription().strip();
            double price = item.getPrice();
            if (description.length() % 3 == 0) {
                int itemPoints = (int) Math.ceil(price * 0.2);
                points += itemPoints;
                breakdown.add(itemPoints + " points - \"" + description + "\" is " + description.length() + " characters (a multiple of 3)");
            }
        }

        // Rule: 6 points if the day of purchase is odd
        LocalDate purchaseDate = LocalDate.parse(receipt.getPurchaseDate());
        if (purchaseDate.getDayOfMonth() % 2 != 0) {
            points += 6;
            breakdown.add("6 points - purchase day is odd");
        }

        // Rule: 10 points if purchase time is between 2:00pm and 4:00pm
        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime());
        if (purchaseTime.getHour() >= 14 && purchaseTime.getHour() < 16) {
            points += 10;
            breakdown.add("10 points - " + purchaseTime + " is between 2:00pm and 4:00pm");
        }

        return Map.of("total_points", points, "breakdown", breakdown);
    }

    public void saveReceipt(String receiptId, Map<String, Object> pointsData) {
        receipts.put(receiptId, pointsData);
    }

    public Map<String, Object> getReceipt(String receiptId) {
        return receipts.get(receiptId);
    }

    public Map<String, Map<String, Object>> getReceipts() {
        return receipts;
    }
}