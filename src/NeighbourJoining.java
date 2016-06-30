import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class NeighbourJoining {
    Map<String, Double> weights = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new NeighbourJoining().run();
    }

    private void run() {
        List<String> names = new ArrayList<>(Arrays.asList("K", "L", "M", "N"));
      //  List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        List<Node> nodes = new ArrayList<>();

        /*weights.put("A-B", 5.);

        weights.put("C-A", 4.);
        weights.put("C-B", 7.);

        weights.put("D-A", 7.);
        weights.put("D-B", 10.);
        weights.put("D-C", 7.);

        weights.put("E-A", 6.);
        weights.put("E-B", 9.);
        weights.put("E-C", 6.);
        weights.put("E-D", 5.);

        weights.put("F-A", 8.);
        weights.put("F-B", 11.);
        weights.put("F-C", 8.);
        weights.put("F-D", 9.);
        weights.put("F-E", 8.);*/

        weights.put("K-L", 16.);
        weights.put("K-M", 16.);
        weights.put("K-N", 16.);
        weights.put("L-M",  8.);
        weights.put("L-N",  8.);
        weights.put("M-N",  4.);


        for (int i = 0; i < names.size(); i++) {
            nodes.add(new Node(names.get(i)));
        }

        while (names.size() > 1) {
            int n = names.size();
            int min_i = 0;
            int min_j = 0;
            double min_distance = Double.POSITIVE_INFINITY;

            double[] m = new double[n];
            for (int i = 0; i < names.size(); i++) {
                double sum = 0;
                for (int j = 0; j < names.size(); j++) {
                    if (i != j) {
                        sum += getDistance(names.get(i), names.get(j));
                    }
                }
                if (n >= 3) {
                    m[i] = sum / (n - 2);
                } else {
                    m[i] = 0;
                }
            }

            for (int i = 0; i < names.size(); i++) {
                for (int j = i+1; j < names.size(); j++) {
                    String n1 = names.get(i);
                    String n2 = names.get(j);
                    double d = getDistance(n1, n2) - m[i] - m[j];
                    if (d < min_distance) {
                        min_distance = d;
                        min_i = i;
                        min_j = j;
                    }
                }
            }

            String aName = names.get(min_i);
            String bName = names.get(min_j);
            double distAB = getDistance(aName, bName);

            double dA = 0.5 * (distAB + m[min_i] - m[min_j]);
            double dB = 0.5 * (distAB + m[min_j] - m[min_i]);

            if (n == 2) {
                dA = distAB;
                dB = Double.POSITIVE_INFINITY;
            }

            String newName = aName + bName;
            Node newNode = new Node(
                    newName,
                    nodes.get(min_i),
                    nodes.get(min_j),
                    dA,
                    dB
            );

            names.remove(min_j);
            nodes.remove(min_j);
            names.remove(min_i);
            nodes.remove(min_i);

            names.add(newNode.label);
            nodes.add(newNode);

            for (int i = 0; i < names.size() - 1; i++) {
                String name = names.get(i);
                final double da = getDistance(aName, name);
                final double db = getDistance(bName, name);
                double d = 0.5 * (da + db - distAB);

                weights.put(newName + "-" + name, d);
                remove(aName, name);
                remove(bName, name);
            }
            remove(aName, bName);
        }

        PrettyNode root = new PrettyNode(nodes.get(0));
        root.layout();
        for (int i = 0; i < root.height; i++) {
            System.out.println(root.toString(i));
        }
    }

    private void remove(String n1, String n2) {
        weights.remove(n2 + "-" + n1);
        weights.remove(n1 + "-" + n2);
    }

    private double getDistance(String n1, String n2) {
        Double d1 = weights.get(n1 + "-" + n2);
        if (d1 != null) {
            return d1;
        }
        Double d2 = weights.get(n2 + "-" + n1);
        if (d2 != null) {
            return d2;
        }
        throw new RuntimeException();
    }

    static class Node {
        String label;
        Node left;
        Node right;
        int leafsNumber;
        double distToLeft;
        double distToRight;

        public Node(String l) {
            this.label = l;
            this.leafsNumber = 1;
        }

        public Node(String label, Node left, Node right, double distToLeft, double distToRight) {
            this.label = label;
            this.left = left;
            this.right = right;
            this.leafsNumber = left.leafsNumber + right.leafsNumber;
            this.distToLeft = distToLeft;
            this.distToRight = distToRight;
        }

        @Override
        public String toString() {
            if (left == null) {
                return label;
            }
            double dl = this.distToLeft;
            dl -= left.distToLeft;
            double dr = this.distToRight;
            dr -= right.distToLeft;
            StringBuilder builder = new StringBuilder();
            builder.append("(").append(label).append(" ");
            builder.append(dl).append(":").append(left).append(" ");
            builder.append(dr).append(":").append(right);
            builder.append(")");
            return builder.toString();
        }
    }

    static class PrettyNode {
        String label;
        PrettyNode left;
        PrettyNode right;
        double distToLeft;
        double distToRight;
        public int y = 0;
        public int middle = 0;
        public int height;

        PrettyNode(Node node) {
            this.label = node.label;
            if (node.left != null) {
                this.label = node.label;
                this.left = new PrettyNode(node.left);
                this.right = new PrettyNode(node.right);
                this.distToLeft = node.distToLeft;
                this.distToRight = node.distToRight;
            }
        }

        void layout() {
            if (left != null) {
                left.layout();
                right.layout();
                height = left.height + right.height + 1;
                middle += left.height;
                right.moveY(left.height + 1);
            } else {
                height = 1;
            }
        }

        public String toString(int y) {
            if (left != null) {
                String result = "";

                if (y > left.middle && y < right.middle) {
                    result += "|";
                } else if (y == left.middle || y == right.middle) {
                    result += "+";
                } else {
                    result += " ";
                }

                if (left.y <= y && y < left.y + left.height) {
                    int h = (int) (distToLeft * 8);
                    if (Double.isInfinite(distToLeft)) {
                        h = 4;
                    }
                    if (y == left.middle) {
                        result += "-" + format(distToLeft);
                        while (result.length() < h) {
                            result += "-";
                        }
                    } else {
                        for (int i = 0; i < h - 1; i++) {
                            result += " ";
                        }
                    }
                    result += left.toString(y);
                }
                if (right.y <= y && y < right.y + right.height) {
                    int h = (int) (distToRight * 8);
                    if (Double.isInfinite(distToRight)) {
                        h = 8;
                    }
                    if (y == right.middle) {
                        result += "-" + format(distToRight);
                        while (result.length() < h) {
                            result += "-";
                        }
                    } else {
                        for (int i = 0; i < h - 1; i++) {
                            result += " ";
                        }
                    }
                    result += right.toString(y);
                }
                return result;
            } else {
                return label;
            }
        }

        private String format(double v) {
            DecimalFormat format = new DecimalFormat("#.##");
            String result;
            if (Double.isInfinite(v)) {
                result = "";
            } else {
                result = format.format(v);
            }
            return result;
        }

        public void moveY(int dy) {
            this.y += dy;
            this.middle += dy;
            if (left != null) {
                left.moveY(dy);
                right.moveY(dy);
            }
        }
    }
}