import java.util.*;

class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String L = in.next();
        int N = in.nextInt();
        Encryptor encryptor = new Encryptor();
        for (int i = 0; i < N; i++) {
            String W = in.next();
            encryptor.addWordToDict(W);
        }
        System.err.println("word is " + L);
        System.err.println(encryptor.dict);
        long result = encryptor.solve(L, 0);
        System.err.println("Cache matches: "+ encryptor.getCacheMatches());
        System.out.println(result);
    }


    public static class Encryptor {
        private final static String[] morse = {
                ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....",
                "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
                "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-",
                "-.--", "--.."
        };

        private final Map<String, Integer> dict = new HashMap<>();
        private final Map<Integer, Long> cache = new HashMap<>();
        private int maxLen = 80;

        private int foundInCache = 0;

        public void addWordToDict(String word) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                int letter = word.charAt(i) - 'A';
                String dLetter = morse[letter];
                sb.append(dLetter);
            }
            String key = sb.toString();
            maxLen = Math.max(maxLen, key.length());
            if (dict.containsKey(key)) {
                dict.put(key, dict.get(key) + 1);
            } else {
                dict.put(key, 1);
            }
        }

        public long solve(String word, int start) {
            if (start == word.length()) {
                return 1;
            }

            if (cache.containsKey(start)) {
                foundInCache++;
                return cache.get(start);
            }

            long res = 0;
            for (int i = 1; i <= maxLen && start + i <= word.length(); i++) {
                Integer n = dict.getOrDefault(word.substring(start, start + i), null);

                if (n != null) {
                    res += (long) n * solve(word, start + i);
                }
            }
            cache.put(start, res);
            return res;
        }

        public int getCacheMatches() {
            return foundInCache;
        }

    }
}