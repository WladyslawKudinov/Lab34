package EpicLabFolder.AddOn;

import EpicLabFolder.OldCodebase.ReactorOld;
import EpicLabFolder.OldCodebase.StorageOfReactorsOld;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReadFromDB {

    public static void fetchFromDatabase(String filePath, ReactorHolderNew reactorHolderNew, StorageOfReactorsOld storageOfReactorsOld) throws IOException, SQLException {
        String jdbcUrl = "jdbc:sqlite:" + filePath.replace("\\", "\\\\");
        Connection conn = null;
        Map<String, ReactorOld> reactorTypeMap = storageOfReactorsOld.getReactorMap();
        try {
            conn = DriverManager.getConnection(jdbcUrl);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ReactorsPris");

            while (rs.next()) {
                String reactorName = rs.getString("name");
                String type = rs.getString("type");
                String country = rs.getString("country");
                String region = fetchRegion(conn, country);
                String operator = rs.getString("operator");
                int burnup = fetchBurnup(conn, type);
                int thermalCapacity = rs.getInt("thermalCapacity");
                int firstGridConnection = rs.getInt("firstGridConnection");
                HashMap<Integer, Double> loadFactors = fetchLoadFactors(conn, reactorName);

                if (reactorTypeMap.containsKey(type)) {
                    ReactorOld reactorOld = reactorTypeMap.get(type);
                    burnup = reactorOld.getBurnup().intValue();
                    thermalCapacity = reactorOld.getThermalCapacity().intValue();
                }

                ReactorNew reactorNew = new ReactorNew(reactorName, type, country, region, operator, burnup, thermalCapacity, firstGridConnection, loadFactors);
                reactorHolderNew.addReactor(reactorNew);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw e;
                }
            }
        }
    }

    private static String fetchRegion(Connection conn, String country) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT region FROM Countries WHERE country = ?");
        pstmt.setString(1, country);
        ResultSet rs = pstmt.executeQuery();
        return rs.getString("region");
    }

    private static int fetchBurnup(Connection conn, String type) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT burnup FROM ReactorsTypes WHERE type = ?");
        pstmt.setString(1, type);
        ResultSet rs = pstmt.executeQuery();
        return rs.getInt("burnup");
    }

    private static HashMap<Integer, Double> fetchLoadFactors(Connection conn, String reactorName) throws SQLException {
        HashMap<Integer, Double> loadFactorsMap = new HashMap<>();
        PreparedStatement pstmt = conn.prepareStatement("SELECT year, loadfactor FROM LoadFactor WHERE reactor = ?");
        pstmt.setString(1, reactorName);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int year = rs.getInt("year");
            double loadFactor = rs.getDouble("loadfactor");
            loadFactorsMap.put(year, loadFactor);
        }
        return loadFactorsMap;
    }
}
