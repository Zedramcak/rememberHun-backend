#!/bin/bash
# dev.sh - Docker Compose convenience script for RememberHun development

GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'


echo_info() {
  echo -e "${BLUE}INFO:${NC} $1"
}

echo_success() {
  echo -e "${GREEN}SUCCESS:${NC} $1"
}


case "$1" in
  start)
    echo_info "Starting development environment..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
    echo_success "Development environment started"
    ;;
  stop)
    echo_info "Stopping development environment..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml down
    echo_success "Development environment stopped"
    ;;
  restart)
    echo_info "Restarting development environment with rebuild..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml down
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build
    echo_success "Development environment restarted"
    ;;
  rebuild)
    echo_info "Rebuilding app container only..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build app
    echo_success "App container rebuilt and restarted"
    ;;
  logs)
    echo_info "Showing app logs (Ctrl+C to exit)..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml logs -f app
    ;;
  db)
    echo_info "Connecting to PostgreSQL database..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml exec db psql -U postgres -d rememberHun
    ;;
  status)
    echo_info "Checking container status..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml ps
    ;;
  *)
    echo "RememberHun Development Helper"
    echo "------------------------------"
    echo "Usage: $0 {start|stop|restart|rebuild|logs|db|status}"
    echo ""
    echo "Commands:"
    echo "  start   - Start development environment"
    echo "  stop    - Stop development environment"
    echo "  restart - Restart with full rebuild"
    echo "  rebuild - Rebuild app container only"
    echo "  logs    - Show app container logs"
    echo "  db      - Connect to PostgreSQL"
    echo "  status  - Show container status"
    exit 1
    ;;
esac
