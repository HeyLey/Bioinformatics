import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class WUPGMA {
    Map<String, Double> weights = new HashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("WPGMA");
        new WUPGMA().run(Type.WPGMA);
        System.out.println("UPGMA");
        new WUPGMA().run(Type.UPGMA);
    }

    private void run(Type type) {
        List<String> names = new ArrayList<>(Arrays.asList("K", "L", "M", "N"));
      //  List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        List<Node> nodes = new ArrayList<>();


        weights.put("K-L", 16.);
        weights.put("K-M", 16.);
        weights.put("K-N", 16.);
        weights.put("L-M",  8.);
        weights.put("L-N",  8.);
        weights.put("M-N",  4.);

       /* weights.put("A-B",  5.);
        weights.put("A-C",  4.);
        weights.put("B-C",  7.);
        weights.put("D-A",  7.);
        weights.put("D-B",  10.);
        weights.put("D-C",  7.);
        weights.put("E-A",  6.);
        weights.put("E-B",  9.);
        weights.put("E-C",  6.);
        weights.put("E-D",  5.);
        weights.put("F-A",  8.);
        weights.put("F-B",  11.);
        weights.put("F-C",  8.);
        weights.put("F-D",  9.);
        weights.put("F-E",  8.); */


        for (int i = 0; i < names.size(); i++) {
            nodes.add(new Node(names.get(i)));
        }

        while (names.size() > 1) {
            int min_i = 0;
            int min_j = 0;
            double min_distance = Double.POSITIVE_INFINITY;

            for (int i = 0; i < names.size(); i++) {
                for (int j = i+1; j < names.size(); j++) {
                    String n1 = names.get(i);
                    String n2 = names.get(j);
                    double d = getDistance(n1, n2);
                    if (d < min_distance) {
                        min_distance = d;
                        min_i = i;
                        min_j = j;
                    }
                }
            }

            String left = names.get(min_i);
            String right = names.get(min_j);
            double dist = getDistance(left, right);

            String newName = left + right;
            Node newNode = new Node(
                    newName,
                    nodes.get(min_i),
                    nodes.get(min_j),
                    dist / 2,
                    dist / 2
            );

            names.remove(min_j);
            nodes.remove(min_j);
            names.remove(min_i);
            nodes.remove(min_i);

            names.add(newNode.label);
            nodes.add(newNode);

            for (int i = 0; i < names.size() - 1; i++) {
                String name = names.get(i);
                final double dl = getDistance(left, name);
                final double dr = getDistance(right, name);
                double d = 0;
                if (type == Type.WPGMA) {
                    d = (dl + dr) / 2;
                } else {
                    d = (dl * newNode.left.leafsNumber + dr * newNode.right.leafsNumber) / newNode.leafsNumber;
                }
                weights.put(newName + "-" + name, d);
                remove(left, name);
                remove(right, name);
            }
            remove(left, right);
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
        if (d2 != null){
            return d2;
        }
        throw new RuntimeException();
    }

    enum Type {
        WPGMA,
        UPGMA
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
        public int width;

        PrettyNode(Node node) {
            this.label = node.label;
            if (node.left != null) {
                this.label = node.label;
                this.left = new PrettyNode(node.left);
                this.right = new PrettyNode(node.right);
                this.distToLeft = node.distToLeft - node.left.distToLeft;;
                this.distToRight = node.distToRight - node.right.distToLeft;;
            }
        }

        void layout() {
            if (left != null) {
                left.layout();
                right.layout();
                height = left.height + right.height + 1;
                middle += left.height;
                right.moveY(left.height + 1);

                if (left.width > right.width) {
                    right.width = left.width;
                } else {
                    left.width = right.width;
                }
                width += left.width + 12;
            } else {
                width = label.length();
                height = 1;
            }
        }

        public String toString(int y) {
            if (left != null) {
                int h = width - left.width;
                String result = "";
                if (y == middle) {
                    result += "-";
                } else {
                    result += " ";
                }
                if (y > left.middle && y < right.middle) {
                    result += "|";
                } else if (y == left.middle || y == right.middle) {
                    result += "+";
                } else {
                    result += " ";
                }


                if (y == left.middle) {
                    result += "-" + format(distToLeft) + "--";
                } else if (y == right.middle) {
                    result += "-" + format(distToRight) + "--";
                } else  {
                    for (int i = 0; i < h - 2; i++) {
                        result += " ";
                    }
                }
                if (left.y <= y && y < left.y + left.height) {
                    result += left.toString(y);
                }
                if (right.y <= y && y < right.y + right.height) {
                    result += right.toString(y);
                }
                return result;
            } else {
                int h = width - 1;
                String result = "";
                for (int i = 0; i < h; i++) {
                    result += "-";
                }
                return result + label;
            }
        }

        private String format(double v) {
            DecimalFormat format = new DecimalFormat("#.##");
            String result = "[" + format.format(v) + "]";
            while (result.length() < 7) {
                result += "-";
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