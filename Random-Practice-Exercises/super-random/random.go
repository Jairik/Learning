// Chrome-style dinosaur runner for the terminal.
package main

import (
	"fmt"
	"math/rand"
	"os"
	"strings"
	"time"

	"golang.org/x/term"
)

// Playfield and physics constants.
const (
	fieldWidth  = 72
	fieldHeight = 12
	playerCol   = 8
	groundRow   = fieldHeight - 2 // row index for ground line
	gravity     = 0.6
	jumpVel     = -4.5
	tickMs      = 50
	baseSpeed   = 1.0
)

// Obstacle is a cactus scrolling left across the field.
type Obstacle struct {
	x      float64 // horizontal position (fractional for smooth scroll)
	width  int     // 1 or 2 columns wide
	height int     // 1 or 2 rows tall
}

// Game holds all runtime state for one run.
type Game struct {
	playerY    float64 // 0 = on ground; negative = in air
	playerVY   float64
	obstacles  []Obstacle
	score      int
	best       int
	speed      float64
	spawnGap   int // ticks until next spawn attempt
	gameOver   bool
	quit       bool
	restart    bool
	jumpQueued bool
}

// resetGame clears run state for a new attempt while keeping best score.
func resetGame(g *Game) {
	g.playerY = 0
	g.playerVY = 0
	g.obstacles = nil
	g.score = 0
	g.speed = baseSpeed
	g.spawnGap = 30
	g.gameOver = false
	g.restart = false
	g.jumpQueued = false
}

// main sets up the terminal and runs the game loop.
func main() {
	if !term.IsTerminal(int(os.Stdout.Fd())) {
		fmt.Println("Run this game in a terminal (e.g. go run .)")
		os.Exit(1)
	}

	oldState, err := term.MakeRaw(int(os.Stdin.Fd()))
	if err != nil {
		fmt.Fprintf(os.Stderr, "terminal raw mode: %v\n", err)
		os.Exit(1)
	}
	defer term.Restore(int(os.Stdin.Fd()), oldState)

	// Hide cursor while playing
	fmt.Print("\033[?25l")
	defer fmt.Print("\033[?25h")

	game := &Game{}
	resetGame(game)

	keys := make(chan rune, 8)
	go readKeys(keys)

	ticker := time.NewTicker(tickMs * time.Millisecond)
	defer ticker.Stop()

	rand.Seed(time.Now().UnixNano())

	for !game.quit {
		select {
		case key := <-keys:
			handleKey(game, key)
		case <-ticker.C:
			if game.restart {
				resetGame(game)
			}
			if !game.gameOver {
				update(game)
			}
			render(game)
		}
	}

	// Clear screen and restore a clean prompt line
	fmt.Print("\033[2J\033[H")
	fmt.Printf("Thanks for playing! Best score: %d\n", game.best)
}

// readKeys reads runes from raw stdin and forwards them to the channel.
func readKeys(keys chan<- rune) {
	buf := make([]byte, 3)
	for {
		n, err := os.Stdin.Read(buf)
		if err != nil || n == 0 {
			return
		}
		for _, b := range buf[:n] {
			keys <- rune(b)
		}
	}
}

// handleKey reacts to keyboard input (jump, quit, restart).
func handleKey(g *Game, key rune) {
	switch key {
	case ' ':
		g.jumpQueued = true
	case 'q', 'Q':
		g.quit = true
	case 'r', 'R':
		if g.gameOver {
			g.restart = true
		}
	}
}

// update advances physics, obstacles, score, and checks collisions.
func update(g *Game) {
	// Jump on space when on the ground
	if g.jumpQueued && g.playerY >= -0.01 {
		g.playerVY = jumpVel
	}
	g.jumpQueued = false

	// Gravity and vertical motion (playerY is 0 on ground, negative when airborne)
	g.playerVY += gravity
	g.playerY += g.playerVY
	if g.playerY >= 0 {
		g.playerY = 0
		g.playerVY = 0
	}

	// Move obstacles left
	for i := range g.obstacles {
		g.obstacles[i].x -= g.speed
	}
	g.obstacles = filterObstacles(g.obstacles)

	// Spawn new cacti
	g.spawnGap--
	if g.spawnGap <= 0 && canSpawn(g) {
		spawnObstacle(g)
		g.spawnGap = rand.Intn(40) + 25 - g.score/50
		if g.spawnGap < 15 {
			g.spawnGap = 15
		}
	}

	// Score and difficulty ramp
	g.score++
	if g.score%100 == 0 {
		g.speed += 0.15
	}

	if checkCollision(g) {
		g.gameOver = true
		if g.score > g.best {
			g.best = g.score
		}
	}
}

// filterObstacles drops obstacles that scrolled off the left edge.
func filterObstacles(obs []Obstacle) []Obstacle {
	alive := obs[:0]
	for _, o := range obs {
		if o.x+float64(o.width) > 0 {
			alive = append(alive, o)
		}
	}
	return alive
}

// canSpawn returns true if the right side is clear enough for a new obstacle.
func canSpawn(g *Game) bool {
	for _, o := range g.obstacles {
		if o.x > float64(fieldWidth-20) {
			return false
		}
	}
	return true
}

// spawnObstacle adds a cactus at the right edge with random size.
func spawnObstacle(g *Game) {
	width := 1
	height := 1
	if rand.Float32() < 0.3 {
		width = 2
	}
	if rand.Float32() < 0.25 {
		height = 2
	}
	g.obstacles = append(g.obstacles, Obstacle{
		x:      float64(fieldWidth),
		width:  width,
		height: height,
	})
}

// checkCollision returns true if the dino overlaps any obstacle on the grid.
func checkCollision(g *Game) bool {
	// Dino body: one column wide, two rows tall when on/near ground
	dinoTop := groundRow - 1
	if g.playerY < -1 {
		dinoTop = groundRow - 2
	}
	dinoBottom := groundRow - 1
	if g.playerY < -0.5 {
		dinoBottom = groundRow - 2
	}
	// Convert jump height to row offset (negative playerY = higher on screen)
	jumpRows := int(-g.playerY + 0.5)
	dinoTop -= jumpRows
	dinoBottom -= jumpRows

	for _, o := range g.obstacles {
		left := int(o.x + 0.5)
		right := left + o.width - 1
		obsTop := groundRow - o.height
		obsBottom := groundRow - 1

		// Column overlap
		if playerCol < left || playerCol > right {
			continue
		}
		// Row overlap (AABB on character grid)
		if dinoBottom < obsTop || dinoTop > obsBottom {
			continue
		}
		return true
	}
	return false
}

// render draws the full frame to the terminal using ANSI escapes.
func render(g *Game) {
	// Build empty playfield as rune rows
	rows := make([][]rune, fieldHeight)
	for y := 0; y < fieldHeight; y++ {
		rows[y] = make([]rune, fieldWidth)
		for x := 0; x < fieldWidth; x++ {
			rows[y][x] = ' '
		}
	}

	// Ground line
	for x := 0; x < fieldWidth; x++ {
		rows[groundRow][x] = '_'
	}

	// Obstacles (cacti)
	for _, o := range g.obstacles {
		left := int(o.x + 0.5)
		for dx := 0; dx < o.width; dx++ {
			col := left + dx
			if col < 0 || col >= fieldWidth {
				continue
			}
			for h := 0; h < o.height; h++ {
				row := groundRow - 1 - h
				if row >= 0 && row < fieldHeight {
					rows[row][col] = '#'
				}
			}
		}
	}

	// Dino: ">" on body row, optional second pixel when jumping high
	jumpRows := int(-g.playerY + 0.5)
	dinoRow := groundRow - 1 - jumpRows
	if dinoRow >= 0 && dinoRow < fieldHeight && playerCol < fieldWidth {
		rows[dinoRow][playerCol] = '>'
	}

	var b strings.Builder
	b.WriteString("\033[2J\033[H")
	fmt.Fprintf(&b, "Score: %-6d     BEST: %d\n\n", g.score, g.best)

	for y := 0; y < fieldHeight; y++ {
		b.WriteString(string(rows[y]))
		b.WriteByte('\n')
	}

	if g.gameOver {
		b.WriteString("\n  GAME OVER — press R to restart, Q to quit\n")
	} else {
		b.WriteString("\n  SPACE=jump   Q=quit\n")
	}

	fmt.Print(b.String())
}
