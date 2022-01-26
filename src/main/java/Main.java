import benchmark.propositions.BenchMarkPropositionHasKeywords;
import benchmark.propositions.BenchmarkKeywordHasPropositions;
import data.dao.proposition.KeywordHasProposalsDao;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
       System.out.println("Model : proposition contains keywords");
        Map<String, Long> proposalSelectTime = BenchMarkPropositionHasKeywords.benchmarkSelection();
        Map<String, Long> proposalInsertTime = BenchMarkPropositionHasKeywords.benchmarkInsertion();

        for(Map.Entry<String, Long> entry: proposalSelectTime.entrySet()) {
            System.out.println(entry.getKey() + " in " + entry.getValue() + " ms.");
        }

        for(Map.Entry<String, Long> entry: proposalInsertTime.entrySet()) {
            System.out.println(entry.getKey() + " in " + entry.getValue() + " ms.");
        }

         System.out.println("Model : keyword contains propositions");
        Map<String, Long> keyWordSelectTime = BenchmarkKeywordHasPropositions.benchmarkSelection();
        Map<String, Long> keyWordInsertTime = BenchmarkKeywordHasPropositions.benchmarkInsertion();

        for(Map.Entry<String, Long> entry: keyWordSelectTime.entrySet()) {
            System.out.println(entry.getKey() + " in " + entry.getValue() + " ms.");
        }

        for(Map.Entry<String, Long> entry: keyWordInsertTime.entrySet()) {
            System.out.println(entry.getKey() + " in " + entry.getValue() + " ms.");
        }
    }
}