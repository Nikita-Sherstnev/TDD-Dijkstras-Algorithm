package test;

import com.sherstnyov.PathFinder;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinPathTest {
    private static String ANY = null;

    private void assertMinPath(String graph,
                               Integer length, String path) {
        PathFinder pf = makePathFinder(graph);
        if (length != null)
            assertEquals((int)length, pf.getLength());
        if (path != null)
            assertEquals(path, pf.getPath().toString());
    }

    @Test
    public void noGraph_NoPathZeroLength() throws Exception {
        assertMinPath("", 0, "[]");
    }

    private PathFinder makePathFinder(String graph) {
        PathFinder pf = new PathFinder();
        Pattern edgePattern = Pattern.compile("(\\D+)(\\d+)(\\D+)");
        String[] edges = graph.split(",");
        for (String edge : edges) {
            Matcher matcher = edgePattern.matcher(edge);
            if (matcher.matches()) {
                String start = matcher.group(1);
                int length = Integer.parseInt(matcher.group(2));
                String end = matcher.group(3);
                pf.addEdge(start, end, length);
            }
        }
        pf.findPath("A", "Z");
        return pf;
    }

    @Test
    public void degenerateCases() throws Exception {
        assertMinPath("", 0, "[]");   //empty graph
        assertMinPath("A", 0, "[]");  //one node
        assertMinPath("B1C", 0, "[]");//no start or end
        assertMinPath("A1C", 0, "[]");//no end
        assertMinPath("B1Z", 0, "[]");//no start
    }

    @Test
    public void oneEdge() throws Exception {
        assertMinPath("A2Z", 2, "[A, Z]");
    }

    @Test
    public void twoEdges() throws Exception {
        assertMinPath("A1B,B1Z", 2, "[A, B, Z]");
        assertMinPath("B1Z,A1B", 2, "[A, B, Z]");
        assertMinPath("A1X,Y1Z", 0, "[]");
    }

    @Test
    public void threeEdges() throws Exception {
        assertMinPath("A2B,B3C,C4Z", 9, "[A, B, C, Z]");
        assertMinPath("B3C,C4Z,A2B", 9, "[A, B, C, Z]");
    }

    @Test
    public void fourEdges() throws Exception {
        assertMinPath("A1B,B2C,C3D,C3Z", 6, "[A, B, C, Z]");

    }

    @Test
    public void onlyOnePath() throws Exception {
        assertMinPath("A1B,B2C,C3Z,B4D,D6E", 6, "[A, B, C, Z]");
    }

    @Test
    public void parallelPaths() throws Exception {
        assertMinPath("A1B,B2Z,A1Z", 1, "[A, Z]");
    }
}
