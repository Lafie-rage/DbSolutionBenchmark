package benchmark.propositions;

import data.dao.proposition.ProposalHasKeywordsDao;
import org.bson.Document;

import java.util.*;

abstract public class BenchMarkPropositionHasKeywords {
    // SELECTION
    private static final String FETCH_BY_KEYWORD_WITHOUT_INDEX = "FETCH BY KEYWORD WITHOUT INDEX";
    private static final String FETCH_BY_KEYWORD_WITH_INDEX = "FETCH BY KEYWORD WITH INDEX";
    private static final String FETCH_BY_PROPOSAL_ID_WITHOUT_INDEX = "FETCH BY PROPOSAL ID WITHOUT INDEX";
    private static final String FETCH_BY_PROPOSAL_ID_WITH_INDEX = "FETCH BY PROPOSAL ID WITH INDEX";

    // INSERTION
    private static final String INSERT_1_WITH_1_KEYWORD_KEY = "INSERT 1 PROPOSAL WITH 1 KEYWORD";
    private static final String INSERT_1_WITH_10_KEYWORDS_KEY = "INSERT 1 PROPOSAL WITH 10 KEYWORDS";
    private static final String INSERT_10_WITH_1_KEYWORD_KEY = "INSERT 10 PROPOSALS WITH 1 KEYWORD";
    private static final String INSERT_10_WITH_10_KEYWORDS_KEY = "INSERT 10 PROPOSALS WITH 10 KEYWORDS";

    // Retrieving DAO
    private static final ProposalHasKeywordsDao dao = new ProposalHasKeywordsDao();

    public static Map<String, Long> benchmarkSelection() {
        Map<String, Long> selectTime = new HashMap<>();
        Document filterByKeyword = new Document("keyword", "ut");
        Document filterByProposalId = new Document("id", 256);

        // Fetch by keyword without index
        long beginning = System.nanoTime();
        dao.getWithFilter(filterByKeyword);
        selectTime.put(FETCH_BY_KEYWORD_WITHOUT_INDEX, (System.nanoTime() - beginning) / 1_000_000);

        // Fetch by keyword with index
        // Create index
        dao.createIndexForField("keyword");
        beginning = System.nanoTime();
        dao.getWithFilter(filterByKeyword);
        selectTime.put(FETCH_BY_KEYWORD_WITH_INDEX, (System.nanoTime() - beginning) / 1_000_000);
        // Remove created index
        dao.removeIndexFromField("keyword");

        // Fetch by proposal id without index
        beginning = System.nanoTime();
        dao.getWithFilter(filterByProposalId);
        selectTime.put(FETCH_BY_PROPOSAL_ID_WITHOUT_INDEX, (System.nanoTime() - beginning) / 1_000_000);

        // Fetch by proposal id with index
        // Create index
        dao.createIndexForField("id");
        beginning = System.nanoTime();
        dao.getWithFilter(filterByProposalId);
        selectTime.put(FETCH_BY_PROPOSAL_ID_WITH_INDEX, (System.nanoTime() - beginning) / 1_000_000);
        // Remove created index
        dao.removeIndexFromField("id");

        return selectTime;
    }

    public static Map<String, Long> benchmarkInsertion() {
        Map<String, Long> insertTime = new HashMap<>();

        // Insert one element with 10 keywords
        long beginning = System.nanoTime();
        dao.insert(getProposal(false));
        insertTime.put(INSERT_1_WITH_1_KEYWORD_KEY, (System.nanoTime() - beginning) / 1_000_000);
        dao.removeLast(); // Remove inserted item

        // Insert one element with 10 keywords
        beginning = System.nanoTime();
        dao.insert(getProposal(true));
        insertTime.put(INSERT_1_WITH_10_KEYWORDS_KEY, (System.nanoTime() - beginning) / 1_000_000);
        dao.removeLast(); // Remove inserted item

        beginning = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            dao.insert(getProposal(false));
        }
        insertTime.put(INSERT_10_WITH_1_KEYWORD_KEY, (System.nanoTime() - beginning) / 1_000_000);
        for(int i = 0; i < 10; i++) {
            dao.removeLast(); // Remove inserted items
        }


        beginning = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            dao.insert(getProposal(true));
        }
        insertTime.put(INSERT_10_WITH_10_KEYWORDS_KEY, (System.nanoTime() - beginning) / 1_000_000);
        for(int i = 0; i < 10; i++) {
            dao.removeLast(); // Remove inserted items
        }

        return insertTime;
    }

    private static Document getProposal(boolean with10Keywords) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        final Date beginDate = calendar.getTime();
        final Date endDate = Calendar.getInstance().getTime();
        int lastId = dao.getLastProposalId();

        Document proposal = new Document() // Generating propositions (Not a random one)
                .append("id", lastId + 1)
                .append("userId", 8163)
                .append("beginDate", beginDate)
                .append("endDate", endDate)
                .append("value", 4028.0378)
                .append("isGeneric", true)
                .append("isGoods", true)
                .append("name", "iaculis")
                .append("description", "Etiam vel ut erat tincidunt in et ornare dictum massa Vestibulum in hendrerit metus purus tincidunt vel metus placerat erat vitae ex ut faucibus tortor vel laoreet Etiam quis ")
                .append("competence", new Document()
                        .append("id", 2)
                        .append("name", "velit")
                );

        if(with10Keywords) {
            proposal.append("keyword", Arrays.asList(
                            "ultricies",
                            "dictumst",
                            "ut",
                            "Suspendisse",
                            "tincidunt",
                            "condimentum",
                            "egestas",
                            "porttitor",
                            "mi",
                            "tellus"
                    )
            );
        } else {
            proposal.append("keyword", Collections.singletonList("ut"));
        }
        return proposal;
    }
}
