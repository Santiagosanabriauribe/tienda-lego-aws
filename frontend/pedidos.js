const API = '/api/v1';

const usuario = JSON.parse(localStorage.getItem('usuario'));
if (!usuario) {
    window.location.href = 'login.html';
}

function esc(str) {
    const el = document.createElement('span');
    el.textContent = String(str ?? '');
    return el.innerHTML;
}

function formatoPrecio(precio) {
    return '$ ' + Number(precio).toLocaleString('es-CO') + ' COP';
}

function renderNav() {
    const nav = document.getElementById('nav-links');
    nav.innerHTML = `
        <a href="index.html">Catalogo</a>
        <a href="pedidos.html">Mis Pedidos</a>
        ${usuario.rol === 'ADMIN' ? '<a href="admin.html">Admin</a>' : `<span class="nav-user">${esc(usuario.nombre)}</span>`}
        <a href="#" id="btn-logout">Salir</a>
    `;
    document.getElementById('btn-logout').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('usuario');
        localStorage.removeItem('carritoId');
        window.location.href = 'index.html';
    });
}

function badgeEstado(estado) {
    const clases = {
        'CONFIRMADO': 'estado-confirmado',
        'ENTREGADO': 'estado-entregado',
        'CANCELADO': 'estado-cancelado'
    };
    const textos = {
        'CONFIRMADO': 'No entregado',
        'ENTREGADO': 'Entregado',
        'CANCELADO': 'Cancelado'
    };
    return `<span class="estado-badge ${clases[estado] || ''}">${textos[estado] || estado}</span>`;
}

async function cargarPedidos() {
    const lista = document.getElementById('lista-pedidos');
    try {
        const res = await fetch(`${API}/pedidos/usuario/${usuario.id}`);
        const pedidos = await res.json();

        if (pedidos.length === 0) {
            lista.innerHTML = '<p class="sin-productos">No tienes pedidos aun. <a href="index.html">Ir al catalogo</a></p>';
            return;
        }

        lista.innerHTML = pedidos.map(p => `
            <div class="pedido-card">
                <div class="pedido-card-header">
                    <div>
                        <strong>Pedido #${esc(p.id)}</strong>
                        <span class="pedido-fecha">${new Date(p.fecha).toLocaleDateString('es-CO', { year:'numeric', month:'long', day:'numeric' })}</span>
                    </div>
                    ${badgeEstado(p.estado)}
                </div>
                <div class="pedido-card-items">
                    ${p.items.map(i => `
                        <div class="pedido-item-row">
                            <span>${esc(i.productoNombre)} x${i.cantidad}</span>
                            <span>${formatoPrecio(i.subtotal)}</span>
                        </div>
                    `).join('')}
                </div>
                <div class="pedido-card-footer">
                    <div class="pedido-envio">
                        <span>${esc(p.nombreDestinatario)} - ${esc(p.direccionEnvio)}, ${esc(p.ciudadEnvio)}</span>
                        <span>${esc(p.metodoPago)}</span>
                    </div>
                    <div class="pedido-total">
                        <strong>Total: ${formatoPrecio(p.total)}</strong>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (err) {
        lista.innerHTML = '<p class="sin-productos">Error al cargar los pedidos.</p>';
    }
}

renderNav();
cargarPedidos();
