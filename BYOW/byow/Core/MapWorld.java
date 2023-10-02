package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapWorld {
    public TETile[][] world;
    public TETile[][] darkworld;
    private int width;
    private int height;
    private final int MaxRoomNum = 35;
    private final int MinRoomNum = 20;
    private final int MaxRoomSize = 10;
    private final int MinRoomSize = 4;
    private final long longestSeed = 9223372036854775807L;
    private Random random;
    TreeSet<Position> roomPoints = new TreeSet<>();
    TreeSet<Position> pathPoints = new TreeSet<>();
    TreeSet<Position> wallPoints = new TreeSet<>();
    LinkedList<Room> roomList = new LinkedList<>();
    Position avatar;
    boolean isPlaying = false;

    boolean lighton = true;
    Engine mengine;
    StringBuilder exeC = new StringBuilder();
    String AvaName = "Avatar";
    boolean loaded =false;
    long seed;


    public MapWorld(Engine e, int width, int height) {
        this.world = new TETile[width][height];
        this.darkworld = new TETile[width][height];
        this.width = width;
        this.height = height;
        mengine = e;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                darkworld[x][y] = Tileset.NOTHING;
            }
        }

    }

    public void resetDark() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                darkworld[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void generateRooms(Random r) {
        random = r;
        int roomNum = RandomUtils.uniform(random, MinRoomNum, MaxRoomNum);
        for (int i = 0; i < roomNum; i++) {
            boolean out = false;
            int roomWidth = RandomUtils.uniform(random, MinRoomSize, MaxRoomSize);
            while (roomWidth % 2 == 0) {
                roomWidth = RandomUtils.uniform(random, MinRoomSize, MaxRoomSize);
            }
            int roomHeight = RandomUtils.uniform(random, MinRoomSize, MaxRoomSize);
            while (roomHeight % 2 == 0) {
                roomHeight = RandomUtils.uniform(random, MinRoomSize, MaxRoomSize);
            }
            Position startPos = randomPos(random);
            TreeSet<Position> singleRoomSet = PointSet(roomWidth, roomHeight, startPos.xp, startPos.yp);
            for (Position p : singleRoomSet) {
                for (Position pp : roomPoints) {
                    if ((pp.xp == p.xp && pp.yp == p.yp) || p.xp == width - 1 || p.yp == height - 1) {
                        out = true;
                        break;
                    }
                }
            }
            if (out == true) {
                continue;
            }
            buildRoom(startPos.xp, startPos.yp, roomWidth, roomHeight);
            for (Position e : singleRoomSet) {
                roomPoints.add(e);
            }

            roomList.add(new Room(roomWidth, roomHeight, startPos));
        }
    }

    public void connectRoom() {
        LinkedList<Room> waitList = roomList;
        while (waitList.size() > 1) {
            Room room1 = waitList.removeFirst();
            Room room2 = waitList.removeFirst();
            Position room1Pos = room1.MidPosition();
            Position room2Pos = room2.MidPosition();
            pathPoints.addAll(connectPath(room1Pos.xp, room1Pos.yp, room2Pos.xp, room2Pos.yp, random));
            int guess = random.nextInt(2);
            if (guess == 0) {
                waitList.addLast(room1);
            } else {
                waitList.addLast(room2);
            }
        }
        for (Position p : pathPoints) {
            if (p.xp < width && p.yp < height) {
                world[p.xp][p.yp] = Tileset.FLOOR;
            }
        }
    }

    public void addWall() {
        for (Position p : pathPoints) {
            roomPoints.add(p);
        }
        for (Position p : roomPoints) {
            int x = p.xp;
            int y = p.yp;
            if (x < width && y < height) {
                if (x - 1 > 0 && world[x - 1][y] == Tileset.NOTHING) {
                    world[x - 1][y] = Tileset.WALL;
                    wallPoints.add(new Position(x - 1, y));
                }
                if (x + 1 < width && world[x + 1][y] == Tileset.NOTHING) {
                    world[x + 1][y] = Tileset.WALL;
                    wallPoints.add(new Position(x + 1, y));
                }
                if ((x - 1 > 0 && y + 1 < height) && world[x - 1][y + 1] == Tileset.NOTHING) {
                    world[x - 1][y + 1] = Tileset.WALL;
                    wallPoints.add(new Position(x - 1, y + 1));
                }
                if ((x + 1 < width && y + 1 < height) && world[x + 1][y + 1] == Tileset.NOTHING) {
                    world[x + 1][y + 1] = Tileset.WALL;
                    wallPoints.add(new Position(x + 1, y + 1));
                }
                if (y + 1 < height && world[x][y + 1] == Tileset.NOTHING) {
                    world[x][y + 1] = Tileset.WALL;
                    wallPoints.add(new Position(x, y + 1));
                }
                if ((x - 1 > 0 && y - 1 > 0) && world[x - 1][y - 1] == Tileset.NOTHING) {
                    world[x - 1][y - 1] = Tileset.WALL;
                    wallPoints.add(new Position(x - 1, y - 1));
                }
                if (y - 1 > 0 && world[x][y - 1] == Tileset.NOTHING) {
                    world[x][y - 1] = Tileset.WALL;
                    wallPoints.add(new Position(x, y - 1));
                }
                if ((x + 1 < width && y - 1 > 0) && world[x + 1][y - 1] == Tileset.NOTHING) {
                    world[x + 1][y - 1] = Tileset.WALL;
                    wallPoints.add(new Position(x + 1, y - 1));
                }
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    world[x][y] = Tileset.WALL;
                    wallPoints.add(new Position(x, y));
                }
            }
        }
    }


    public Position randomPos(Random r) {
        int startX = RandomUtils.uniform(random, 0, width - 1);
        int startY = RandomUtils.uniform(random, 0, height - 1);
        return new Position(startX, startY);

    }

    public TreeSet<Position> PointSet(int roomw, int roomh, int startx, int starty) {
        TreeSet<Position> singleRoom = new TreeSet<>();
        for (int x = 0; x < roomw; x++) {
            for (int y = 0; y < roomh; y++) {
                singleRoom.add(new Position(startx + x, starty + y));
            }
        }
        return singleRoom;
    }


    public void buildRoom(int xp, int yp, int w, int h) {
        int xmax = w + xp - 1;
        int ymax = h + yp - 1;
        if (xmax >= width) {
            return;
        }
        if (ymax >= height) {
            return;
        }
        for (int i = xp; i <= w + xp - 1; i++) {
            for (int j = yp; j <= h + yp - 1; j++) {
                world[i][j] = Tileset.FLOOR;
                if (i == xp || j == yp || j == yp + h - 1 || i == xp + w - 1) {
                    world[i][j] = Tileset.NOTHING;     //nothing for now; then changed to WALL
                }
            }
        }
    }

    public TreeSet<Position> connectPath(int x1, int y1, int x2, int y2, Random random) {
        TreeSet<Position> path = new TreeSet<>();
        int x_displace = x2 - x1;
        int y_displace = y2 - y1;
        int x_increment;
        int y_increment;
        if (x_displace == 0) {
            x_increment = 0;
        } else {
            x_increment = x_displace / Math.abs(x_displace);
        }
        if (y_displace == 0) {
            y_increment = 0;
        } else {
            y_increment = y_displace / Math.abs(y_displace);
        }
        int turn = random.nextInt(2);
        int x = x1;
        int y = y1;
        if (turn == 0) {
            int x_turn = x1 + RandomUtils.uniform(random, 0, (Math.abs(x_displace) + 1)) * x_increment;
            while (x != x_turn && x <= width) {
                x = x + x_increment;
                path.add(new Position(x, y));
            }
            while (y != y2 && y <= height) {
                y = y + y_increment;
                path.add(new Position(x, y));
            }

            while (x != x2 && x <= width) {
                x = x + x_increment;
                path.add(new Position(x, y));
            }
        } else {
            int y_turn = y1 + RandomUtils.uniform(random, 0, (Math.abs(y_displace) + 1)) * y_increment; //opposite z shape
            while (y != y_turn && y <= height) {
                y = y + y_increment;
                path.add(new Position(x, y));
            }
            while (x != x2 && x <= width) {
                x = x + x_increment;
                path.add(new Position(x, y));
            }

            while (y != y2 && y <= height) {
                y = y + y_increment;
                path.add(new Position(x, y));
            }
        }
        return path;
    }

    public boolean floornearBy(Position p) {
        if ((p.xp + 1 < width) && (p.xp - 1 > 0) && (p.yp + 1 < height) && (p.yp - 1 > 0)) {
            if (world[p.xp][p.yp + 1] == Tileset.FLOOR || world[p.xp][p.yp - 1] == Tileset.FLOOR || world[p.xp + 1][p.yp] == Tileset.FLOOR || world[p.xp - 1][p.yp] == Tileset.FLOOR) {
                return true;
            }
        }
        return false;
    }

    public void createDoorAndPlayer() {
        Position[] wallArray = wallPoints.toArray(new Position[wallPoints.size()]);
        Position[] floorArray = roomPoints.toArray(new Position[roomPoints.size()]);
        int randomWallIndex = RandomUtils.uniform(random, 0, wallArray.length - 1);
        int randomPlayerIndex = RandomUtils.uniform(random, 0, floorArray.length - 1);
        Position doorPos = wallArray[randomWallIndex];
        Position playerPos = floorArray[randomPlayerIndex];
        while (!(doorPos.xp < width && doorPos.yp < height && floornearBy(doorPos))) {
            randomWallIndex = RandomUtils.uniform(random, 0, wallArray.length - 1);
            doorPos = wallArray[randomWallIndex];
        }
        while (!(playerPos.xp < width && playerPos.yp < height && world[playerPos.xp][playerPos.yp] == Tileset.FLOOR)) {
            randomPlayerIndex = RandomUtils.uniform(random, 0, floorArray.length - 1);
            playerPos = floorArray[randomPlayerIndex];
        }
        world[doorPos.xp][doorPos.yp] = Tileset.LOCKED_DOOR;
        world[playerPos.xp][playerPos.yp] = Tileset.AVATAR;
        avatar = playerPos;
    }


    public void generateMap(long seed) {
        random = new Random();
        random.setSeed(seed);
        this.generateRooms(random);
        this.connectRoom();
        this.addWall();
        this.createDoorAndPlayer();
    }

    //phase 2

    public void drawInterface() {
        int mid_w = width / 2;
        int mid_h = height / 2;
        drawTitle();
        Font option = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(option);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(mid_w, mid_h, "New Game (N)");
        StdDraw.text(mid_w, mid_h - 2, "Load Game (L)");
        StdDraw.text(mid_w, mid_h - 4, "Quit (Q)");
        StdDraw.text(mid_w, mid_h - 6, "Menu (M)");
        StdDraw.show();
    }

    public void drawTitle() {
        StdDraw.clear(Color.black);
        Font title = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(0.5 * width, 0.8 * height, "CS61B: THE GAME");
    }

    public void coverHud() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(3.5, 32, 5, 1);
        StdDraw.setPenColor(Color.WHITE);
    }

    public void drawMouse() {
        coverHud();
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        Position mousePos = new Position(mouseX, mouseY); // check boundary needed?
        if (mousePos.xp >= 0 && mousePos.xp < width && mousePos.yp >= 0 && mousePos.yp < height) {
            TETile mouseT = world[mouseX][mouseY];
            if (mouseT == Tileset.NOTHING) {
                StdDraw.text(4, 32, "Nothing");
            } else if (mouseT == Tileset.WALL) {
                StdDraw.text(4, 32, "Wall");
            } else if (mouseT == Tileset.FLOOR) {
                StdDraw.text(4, 32, "Floor");
            } else if (mouseT == Tileset.LOCKED_DOOR) {
                StdDraw.text(4, 32, "Locked door");
            } else if (mouseT == Tileset.AVATAR) {
                StdDraw.text(4, 32, AvaName);
            }
            StdDraw.show();
        }
    }

    public void drawSeed() {
        StdDraw.clear(Color.black);
        drawTitle();
        Font s = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(s);
        StdDraw.text(0.5 * width, 0.5 * height, "Please enter a seed between 0 to 9,223,372,036,854,775,807");
        StdDraw.text(0.5 * width, 0.5 * height - 2, "Start (S)");
        StdDraw.text(0.5 * width, 0.5 * height - 4, "Quit (Q)");
    }

    public long seedInput() {
        drawSeed();
        StdDraw.show();
        long seed = 0;
        boolean finished = false;
        while (finished == false) {
            if (StdDraw.hasNextKeyTyped()) {
                char a = StdDraw.nextKeyTyped();
                if (a == 'Q' || a == 'q') {
                    System.exit(0);
                } else if (a == 'S' || a == 's') {
                    finished = true;
                } else if (a >= '0' && a <= '9') {
                    //edge case for greater than maximum?
                    seed = seed * 10 + a - '0';   //@chatgpt
                    StdDraw.textLeft(0.3 * width, 0.5 * height + 1, Long.toString(seed));
                    StdDraw.show();
                }
            }
        }
        return seed;
    }

    public void drawMenu() {
        StdDraw.clear(Color.black);
        Font s = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(s);
        StdDraw.text(0.5 * width, 0.5 * height, "Please enter a name for Avatar");
        StdDraw.text(0.5 * width, 0.5 * height - 2, "Finished (:F)");
        StdDraw.text(0.5 * width, 0.5 * height - 4, "Back (:B)");
    }

    public void nameInput() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        drawMenu();
        StdDraw.show();
        boolean finished = false;
        boolean breakpoint = false;
        while (finished == false) {
            if (StdDraw.hasNextKeyTyped()) {
                char a = StdDraw.nextKeyTyped();
                if (a == ':') {
                    breakpoint = true;
                }
                if (breakpoint == false) {
                    sb1.append(a);
                    sb2.append(a);
                } else {
                    sb2.append(a);
                }
                if ((sb2.length() > 1) && (a == 'F' || a == 'f') && (sb2.charAt(sb2.length() - 2) == ':')) {
                    finished = true;
                    AvaName = sb1.toString();
                    drawInterface();
                } else if ((sb2.length() > 1) && (a == 'B' || a == 'b') && (sb2.charAt(sb2.length() - 2) == ':')) {
                    finished = true;
                    drawInterface();
                } else {
                    StdDraw.textLeft(0.3 * width, 0.5 * height + 3, sb1.toString());
                    StdDraw.show();
                }
            }
        }
    }


    public void saveGame() {
        File f = new File("save_data.txt");
        File f2 = new File("save_seed.txt");
        File f3 = new File("save_me.txt");
        int mark =0;
        StringBuilder saveSeed = new StringBuilder();
        boolean go = true;
        try {
            String localseed = "";
            if (!f.exists()) {
                f.createNewFile();
            }
            if (!f2.exists()) {
                f2.createNewFile();
            }
            try (FileWriter fw = new FileWriter(f)) {
                for (int i=0; i<exeC.length();i++){
                    if (exeC.charAt(0)=='n' && exeC.charAt(i) == 's') {
                        mark = i+1;
                        break;
                    }
                }
                String save = exeC.substring(mark, exeC.length() - 2);
                 localseed = exeC.substring(0, mark);
                fw.write(save);
            }

            try (Scanner scanner4 = new Scanner(f2)) {
                while (scanner4.hasNextLine()) {
                    String b = scanner4.nextLine();
                    saveSeed.append(b);
                    System.out.println(b+'b');
                }
            }

                    try (FileWriter fw2 = new FileWriter(f2)) {
//                            while (scanner4.hasNextLine()) {
//                                String b = scanner4.nextLine();
//                                saveSeed.append(b);
//                                System.out.println(b+'b');
//                            }
                            System.out.println(localseed);
                            if((saveSeed.isEmpty() == false) && (localseed.length()!=0)){
                                fw2.write(localseed);
                            }
                            else {
                                saveSeed.append(localseed);
                                System.out.println(saveSeed.toString() + "h");
                                fw2.write(saveSeed.toString());
                            }
                        }


        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0); // exit issue
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0); // exit issue
        }
    }

    public String loadGame() {
        File f = new File("save_data.txt");
        File f2 = new File("save_seed.txt");
        StringBuilder save = new StringBuilder();
        try {
            if (f.exists()) {
                if (f2.exists()) {
                    try (Scanner scanner2 = new Scanner(f2)) {
                        while (scanner2.hasNextLine()) {
                            save.append(scanner2.nextLine());
                        }
                    }
                }
                try (Scanner scanner = new Scanner(f)) {
                    String a = "";

                    if (scanner.hasNextLine()) {
                        a = scanner.next();
                    }
                    if (a != "n") {
                        while (scanner.hasNext()) {
                            save.append(a);
                            a=scanner.next();
                            }
                        save.append(a);
                        }
                    }
            } else {
                System.out.println("Save file not found");
                return null;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
        return save.toString();
    }


    public void move(Position p) {
        if (world[p.xp][p.yp] == Tileset.WALL) {
            return;
        }
        if (world[p.xp][p.yp] == Tileset.LOCKED_DOOR) {
            lighton = !lighton;
            return;
        }
        world[avatar.xp][avatar.yp] = Tileset.FLOOR;
        world[p.xp][p.yp] = Tileset.AVATAR;
        avatar = p;
        darkGenerate();
    }

    public void darkGenerate() {
        resetDark();
        List<Position> lightPoints = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8 - i; j++) {
                if (avatar.plus(new Position(i, j)).xp >= 0 && avatar.plus(new Position(i, j)).xp < width &&
                        avatar.plus(new Position(i, j)).yp >= 0 && avatar.plus(new Position(i, j)).yp < height) {

                    lightPoints.add(avatar.plus(new Position(i, j)));
                }
                if (avatar.plus(new Position(-i, j)).xp >= 0 && avatar.plus(new Position(-i, j)).xp < width &&
                        avatar.plus(new Position(-i, j)).yp >= 0 && avatar.plus(new Position(-i, j)).yp < height) {
                    lightPoints.add(avatar.plus(new Position(-i, j)));
                }
                if (avatar.plus(new Position(i, -j)).xp >= 0 && avatar.plus(new Position(i, -j)).xp < width &&
                        avatar.plus(new Position(i, -j)).yp >= 0 && avatar.plus(new Position(i, -j)).yp < height) {
                    lightPoints.add(avatar.plus(new Position(i, -j)));
                }
                if (avatar.plus(new Position(-i, -j)).xp >= 0 && avatar.plus(new Position(-i, -j)).xp < width &&
                        avatar.plus(new Position(-i, -j)).yp >= 0 && avatar.plus(new Position(-i, -j)).yp < height) {
                    lightPoints.add(avatar.plus(new Position(-i, -j)));
                }
            }
            for (Position p : lightPoints) {
                darkworld[p.xp][p.yp] = world[p.xp][p.yp];
            }
        }
    }

    public void showMap() {
        isPlaying = true;
        if (!lighton) {
            StdDraw.clear(Color.black);
            mengine.ter.renderFrame(darkworld);
        } else {
            mengine.ter.renderFrame(world);
        }
    }

    public void keyExecute(char c) {
        List<Character> keySet = new ArrayList<>();
        keySet.add('w');
        keySet.add('a');
        keySet.add('s');
        keySet.add('d');
        keySet.add('q');
        keySet.add('n');
        keySet.add('l');
        keySet.add(':');
        c = Character.toLowerCase(c);
        if (keySet.contains(c)) {
            exeC.append(c);
        }
        if (isPlaying) {
            if (c == 'w') {
                move(avatar.plus(new Position(0, 1)));
            } else if (c == 'a') {
                move(avatar.plus(new Position(-1, 0)));
            } else if (c == 's') {
                move(avatar.plus(new Position(0, -1)));
            } else if (c == 'd') {
                move(avatar.plus(new Position(1, 0)));
            } else if (c == 't') {
                lighton = !lighton;
                darkGenerate();
            } else if (c == 'q') {
                if (exeC.charAt(exeC.length() - 2) == ':') {
                    saveGame();
                    System.exit(0);    // exit issue
                }
            }
        } else {
            if (c == 'n') {
                seed = seedInput();
                generateMap(seed);
                exeC.append(seed);
                exeC.append('s');
                showMap();
            } else if (c == 'q') {
                System.exit(0);     //exit issue
            } else if (c == 'm') {
                nameInput();
            } else if (c == 'l') {
                String pre = loadGame();
                if (pre.length() > 0) {
                    MapWorld curMap = mengine.helper(pre);
                    world = curMap.world;
                    showMap();
                    while (1 == 1) {
                        if (curMap.isPlaying) {
                            curMap.drawMouse();
                        }
                        if (StdDraw.hasNextKeyTyped()) {
                            char typed = StdDraw.nextKeyTyped();
                            curMap.keyExecute(typed);
                            if (curMap.isPlaying) {
                                curMap.showMap();
                            }
                        }
                    }
                } else {
                    StdDraw.text(0.5 * width, 0.2 * height, "No Save");
                }
            }
        }
    }
}



