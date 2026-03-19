import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '1m', target: 100 },
    { duration: '1m', target: 300 },
    { duration: '2m', target: 500 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<1000'],
  },
};

export default function () {
  const payload = JSON.stringify({
    documentName: `doc-${Date.now()}`,
    content: 'sample content for load testing'
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'x-api-key': 'test-key'
    },
  };

  const res = http.post('http://localhost:8080/api/jobs', payload, params);

  check(res, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
  });

  sleep(1);
}