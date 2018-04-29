# generation of randomly distributed points from an ellipsoid

generate <- function(axes, num_points=10) {
  num_dim <- length(axes)
  S <- diag(axes^2)
  points <- matrix(rnorm(num_dim*num_points), ncol=num_dim)
  points <- points / apply(points, 1, function(r) sqrt(sum(r^2)))
  R <- runif(num_points)^(1/num_dim)
  unif_sphere <- R * points
  T <- chol(S)
  unif_ellipse <- unif_sphere %*% T
}

draw_elipsis2d <- function(axes, res = 1000) {
  max <- max(axes)
  x <- seq(-axes[1], axes[1], 2*axes[1]/res)
  y <- sqrt(axes[2]^2*(1- (x/axes[1])^2))
  plot(y ~ x, xlim=c(-max, max), ylim=c(-max, max), col="red", type="l")
  lines(-y ~ x, col="red")
}

test3d <- function(axes, num_points=1000) {
  g <- generate(axes, num_points)
  plot_ly(x=g[, 1], y=g[, 2], z=g[, 3], type="scatter3d", mode="markers")
}

library(plotly)

test2d <- function(axes, num_points) {
  draw_elipsis(axes)
  points(generate(axes, num_points), col="blue")
}