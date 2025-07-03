import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
export const options = {
    thresholds: {
        http_req_duration: ["p(99) < 50"],
        http_req_failed: ["rate < 0.01"],
    },
    scenarios: {
        get: {
            stages: [
                { duration: "30s", target: 15 },
                { duration: "1m", target: 150 },
                { duration: "20s", target: 0 },
            ],
            exec: "get",
            executor: "ramping-vus"
        }
    }
};

// Simulated user behavior
export function get() {
    let res = http.get("http://localhost:8080/books");
    // Validate response status
    check(res, { "status was 200": (r) => r.status == 200 });
    sleep(1);
}

export function post() {
    let payload = JSON.stringify({
        title: "New Book",
        author: "Author Name",
    });
    let params = {
        headers: {
            "Content-Type": "application/json",
        },
    };
    let res = http.post("http://localhost:8080/books", payload, params);
    // Validate response status
    check(res, { "status was 201": (r) => r.status == 201 });
    sleep(1);
}