package benchmark.propositions;

import data.dao.proposition.KeywordHasProposalsDao;
import org.bson.Document;

import java.util.*;

abstract public class BenchmarkKeywordHasPropositions {
    // SELECTION
    private static final String FETCH_BY_KEYWORD_WITHOUT_INDEX = "FETCH BY KEYWORD WITHOUT INDEX";
    private static final String FETCH_BY_KEYWORD_WITH_INDEX = "FETCH BY KEYWORD WITH INDEX";
    private static final String FETCH_BY_PROPOSAL_ID_WITHOUT_INDEX = "FETCH BY PROPOSAL ID WITHOUT INDEX";
    private static final String FETCH_BY_PROPOSAL_ID_WITH_INDEX = "FETCH BY PROPOSAL ID WITH INDEX";

    // INSERTION
    private static final String INSERT_1_WITH_1_PROPOSAL_KEY = "INSERT 1 KEYWORD WITH 1 PROPOSAL";
    private static final String INSERT_1_WITH_10_PROPOSALS_KEY = "INSERT 1 KEYWORD WITH 10 PROPOSALS";
    private static final String INSERT_10_WITH_1_PROPOSAL_KEY = "INSERT 10 KEYWORDS WITH 1 PROPOSAL";
    private static final String INSERT_10_WITH_10_PROPOSAL_KEY = "INSERT 10 KEYWORDS WITH 10 PROPOSALS";

    // Retrieving DAO
    private static final KeywordHasProposalsDao dao = new KeywordHasProposalsDao();

    public static Map<String, Long> benchmarkSelection() {
        Map<String, Long> insertTime = new HashMap<>();
        Document filterByKeyword = new Document("keyword", "ut");
        Document filterByProposalId = new Document("proposals.id", 256);

        // Fetch by keyword without index
        long beginning = System.nanoTime();
        dao.getWithFilter(filterByKeyword);
        insertTime.put(FETCH_BY_KEYWORD_WITHOUT_INDEX, (System.nanoTime() - beginning) / 1_000_000);

        // Fetch by keyword with index
        // Create index
        dao.createIndexForField("keyword");
        beginning = System.nanoTime();
        dao.getWithFilter(filterByKeyword);
        insertTime.put(FETCH_BY_KEYWORD_WITH_INDEX, (System.nanoTime() - beginning) / 1_000_000);
        // Remove created index
        dao.removeIndexFromField("keyword");

        // Fetch by proposal id without index
        beginning = System.nanoTime();
        dao.getWithFilter(filterByProposalId);
        insertTime.put(FETCH_BY_PROPOSAL_ID_WITHOUT_INDEX, (System.nanoTime() - beginning) / 1_000_000);

        // Fetch by proposal id with index
        // Create index
        dao.createIndexForField("proposals.id");
        beginning = System.nanoTime();
        dao.getWithFilter(filterByProposalId);
        insertTime.put(FETCH_BY_PROPOSAL_ID_WITH_INDEX, (System.nanoTime() - beginning) / 1_000_000);
        // Remove created index
        dao.removeIndexFromField("proposals.id");

        return insertTime;
    }

    public static Map<String, Long> benchmarkInsertion() {
        Map<String, Long> insertTime = new HashMap<>();

        // Insert one element with 10 keywords
        long beginning = System.nanoTime();
        dao.insert(getKeyWord(false, "last"));
        insertTime.put(INSERT_1_WITH_1_PROPOSAL_KEY, (System.nanoTime() - beginning) / 1_000_000);
        dao.removeLast("last"); // Remove inserted item

        // Insert one element with 10 keywords
        beginning = System.nanoTime();
        dao.insert(getKeyWord(true, "last"));
        insertTime.put(INSERT_1_WITH_10_PROPOSALS_KEY, (System.nanoTime() - beginning) / 1_000_000);
        dao.removeLast("last"); // Remove inserted item

        beginning = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            dao.insert(getKeyWord(false, "last" + i));
        }
        insertTime.put(INSERT_10_WITH_1_PROPOSAL_KEY, (System.nanoTime() - beginning) / 1_000_000);
        for(int i = 0; i < 10; i++) {
            dao.removeLast("last" + i); // Remove inserted items
        }


        beginning = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            dao.insert(getKeyWord(true, "last" + i));
        }
        insertTime.put(INSERT_10_WITH_10_PROPOSAL_KEY, (System.nanoTime() - beginning) / 1_000_000);
        for(int i = 0; i < 10; i++) {
            dao.removeLast("last" + i); // Remove inserted items
        }

        return insertTime;
    }

    private static Document getKeyWord(boolean with10Proposals, String keywordValue) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        final Date beginDate = calendar.getTime();
        final Date endDate = Calendar.getInstance().getTime();
        int lastId = dao.getLastProposalId();
        int nbOfProposals = with10Proposals ? 10 : 1;
        List<Document> proposals = new ArrayList<>();

        for(int i = 0; i < nbOfProposals; i++) {
            proposals.add(
                    new Document()
                            .append("id", lastId + 1 + i)
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
                            )
            );
        }

        return new Document() // Generating keyword (Not a random one)
                .append("keyword", keywordValue)
                .append("proposals", proposals);
    }
}
