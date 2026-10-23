public class PacketSymmetryAuditor {
    public static void main(String[] args) {

        String packet = "MADAM";

        TransactionStack stack =
                new TransactionStack(packet.length());

        RequestQueue queue =
                new RequestQueue(packet.length());

        System.out.println("Packet Payload: " + packet);

        // Store characters in both data structures
        for (int i = 0; i < packet.length(); i++) {
            char c = packet.charAt(i);

            stack.push(c);
            queue.enqueue(c);
        }

        boolean isPalindrome = true;

        // Compare stack and queue characters
        while (!stack.isEmpty() && !queue.isEmpty()) {
            char stackChar = stack.pop();
            char queueChar = queue.dequeue();

            System.out.println(
                "Stack: " + stackChar +
                " | Queue: " + queueChar
            );

            if (stackChar != queueChar) {
                isPalindrome = false;
                break;
            }
        }

        if (isPalindrome) {
            System.out.println(
                "Result: Packet is a palindrome."
            );
        } else {
            System.out.println(
                "Result: Packet is NOT a palindrome."
            );
        }
    }
}