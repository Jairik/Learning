/* Super duper basic syntax review for Go. This is not meant to be comprehensive, just a quick refresher on the basics. */

// Every file has an entry point, usually main  
package main

// Important packages 
import (
	"fmt"  // for printing to the console
	"strings" // for string manipulation
)

// Three ways to declare variables
var x int = 5  // Explicit type declaration
var y = 5  // Inferred casting 
var z int  // Default value of 0
//z:= 5  // Sort form for declaring, only inside functions

/* Basic data types:
* int, int32, int64, uint, float32, float64, bool, string, byte (uint8), rune (int32)
*/

func add(a int, b int) int {
	return a + b
}

func divmod(a, b int) (int, int) {
	return a / b, a % b
}

func named() (result int, err error) {
	result = 42
	return  // 'naked' return, which uses the named values (pretty sweet!)
}

func main() {
	// Print to console
	fmt.Println("Hello, World!")

	// Error Handling
	data, err := named()
	if err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Println("Data:", data)
	}

	//Loops and whatnot
	if x := computeSomething(); x > 10 {
		fmt.Println("x is greater than 10")
	}
	for i := 0; i < 5; i++ {
		fmt.Println(i)
	}

	for x < 100 { x *= 2 }

	for { break }

	for i, v := range []string{"a", "b", "c"} {
		fmt.Println(i, v)
	}
	
	// Arrays, slices, maps
	nums := []int{1, 2, 3}
	nums = append(nums, 4)  // Slices can be appended to
	m := map[string]int{"a": 1, "b": 2}
	m["c"] = 3  // Maps can be modified
  
	empty := make([]int, 0, 10)  // Create an empty slice with capacity 10
	
	// Pointers
	x := 10
	p := &x  // p is a pointer to x
	*p = 20  // dereference and modify x through the pointer
	fmt.Println(x)  // prints 20

	// Structs
	type Person struct {
		Name string
		Age  int
	}
	p1 := Person{Name: "Alice", Age: 30}
	fmt.Println(p1)
}
