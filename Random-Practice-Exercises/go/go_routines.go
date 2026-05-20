package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"strings"
	"sync"
	"time"
)

// Reads the API_KEY value from the local .env file
func fetchApiKey() (string, error) {
	// Simulate fetching API key from a secure vault
	data, err := os.ReadFile(".env")
	if err != nil {
		return "", err
	}

	// Get the specific line containing the API key
	for _, line := range strings.Split(string(data), "\n") {
		line = strings.TrimSpace(line)
		if strings.HasPrefix(line, "API_KEY=") {
			return strings.Trim(strings.TrimPrefix(line, "API_KEY="), `"'`), nil
		}
	}

	// If the API key is not found, return an error
	return "", fmt.Errorf("API_KEY not found in .env")
}

// Fetches current temperature for the given city from OpenWeatherMap
func fetchData(city string, apiKey string, ch chan<-string, wg *sync.WaitGroup) interface{} {
	var data struct {
		Main struct {
			Temp float64 `json:"temp"`
		} `json:"main"`
	}

	// Ensure that the WaitGroup counter is decremented when the function completes
	defer wg.Done()

	url := fmt.Sprintf("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, apiKey)
	resp, err := http.Get(url)
	if err != nil {
		fmt.Printf("Error fetching data for %s: %v\n", city, err)
		return nil
	}

	defer resp.Body.Close()

	if err := json.NewDecoder(resp.Body).Decode(&data); err != nil {
		fmt.Printf("Error decoding response for %s: %v\n", city, err)
		return nil
	}

	ch <- fmt.Sprintf("Current temperature in %s: %.2f°C", city, data.Main.Temp)

	return data.Main.Temp
}

// Loads the API key, then fetches and prints weather for each city sequentially
func fetchWeather() {
	apiKey, err := fetchApiKey()
	if err != nil {
		fmt.Printf("Error fetching API key: %v\n", err)
		return
	}
	
	ch := make(chan string)
	var wg sync.WaitGroup

	cities := []string{"New York", "London", "Tokyo", "Sydney"}

	for _, city := range cities {
		wg.Add(1)  // Fetch data for each city in a separate goroutine
		go fetchData(city, apiKey, ch, &wg)
	}

	go func() {
		wg.Wait()  // Wait for all fetchData goroutines to finish
		close(ch)  // Close the channel after all data has been sent
	}()

	for result := range ch {
		fmt.Println(result)  // Print results as they come in
	}

	fmt.Println("Time Took:" , time.Since(time.Now()))
}

func main() {
	fetchWeather()
}