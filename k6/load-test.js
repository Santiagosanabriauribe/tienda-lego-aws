import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const responseTime = new Trend('response_time');

// Configurar la URL del ALB como variable de entorno:
//   k6 run -e BASE_URL=http://<ALB_DNS> k6/load-test.js
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  stages: [
    { duration: '30s', target: 10 },   // rampa hasta 10 usuarios
    { duration: '1m',  target: 50 },   // subir a 50 usuarios
    { duration: '30s', target: 100 },  // pico de 100 usuarios
    { duration: '1m',  target: 100 },  // sostener carga
    { duration: '30s', target: 0 },    // bajar a 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'],  // 95% de las peticiones bajo 2s
    errors: ['rate<0.1'],               // menos de 10% de errores
  },
};

export default function () {
  // 1. Health check
  let healthRes = http.get(`${BASE_URL}/actuator/health`);
  check(healthRes, {
    'health status 200': (r) => r.status === 200,
    'health body UP':    (r) => r.json().status === 'UP',
  });
  errorRate.add(healthRes.status !== 200);
  responseTime.add(healthRes.timings.duration);

  // 2. Listar categorias
  let catRes = http.get(`${BASE_URL}/api/v1/categorias`);
  check(catRes, {
    'categorias status 200': (r) => r.status === 200,
    'categorias es array':   (r) => Array.isArray(r.json()),
  });
  errorRate.add(catRes.status !== 200);
  responseTime.add(catRes.timings.duration);

  // 3. Listar productos
  let prodRes = http.get(`${BASE_URL}/api/v1/productos`);
  check(prodRes, {
    'productos status 200': (r) => r.status === 200,
    'productos es array':   (r) => Array.isArray(r.json()),
  });
  errorRate.add(prodRes.status !== 200);
  responseTime.add(prodRes.timings.duration);

  // 4. Buscar producto por ID (el 1 existe por datos iniciales)
  let detailRes = http.get(`${BASE_URL}/api/v1/productos/1`);
  check(detailRes, {
    'detalle producto 200': (r) => r.status === 200,
  });
  errorRate.add(detailRes.status !== 200);
  responseTime.add(detailRes.timings.duration);

  sleep(1);
}
