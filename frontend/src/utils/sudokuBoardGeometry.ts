import type { CSSProperties } from "react";
import type { GameState } from "../types/game";

export const CELL_SIZE = 52;
export const REGION_GAP = 3;
export const REGION_STROKE_WIDTH = 1;
export const REGION_CORNER_RADIUS = 10;
export const REGION_OUTLINE_INSET = (REGION_GAP + REGION_STROKE_WIDTH) / 2;

type Point = {
  x: number;
  y: number;
};

type Edge = {
  start: Point;
  end: Point;
};

export type RegionOutline = {
  region: number;
  path: string;
};

export type RegionCellPresentation = {
  style: CSSProperties;
};

function getRegionAt(game: GameState, row: number, col: number): number | null {
  if (row < 0 || row >= game.size || col < 0 || col >= game.size) {
    return null;
  }

  return game.regions[row][col];
}

function pointKey(point: Point): string {
  return `${point.x},${point.y}`;
}

function edgeKey(edge: Edge): string {
  return `${pointKey(edge.start)}>${pointKey(edge.end)}`;
}

function addBoundaryEdge(edges: Edge[], start: Point, end: Point) {
  edges.push({ start, end });
}

function toBoardPixels(point: Point): Point {
  return {
    x: point.x * CELL_SIZE,
    y: point.y * CELL_SIZE,
  };
}

function getDistance(start: Point, end: Point): number {
  return Math.hypot(end.x - start.x, end.y - start.y);
}

function pointToward(start: Point, end: Point, distance: number): Point {
  const totalDistance = getDistance(start, end);

  if (totalDistance === 0) {
    return start;
  }

  return {
    x: start.x + ((end.x - start.x) / totalDistance) * distance,
    y: start.y + ((end.y - start.y) / totalDistance) * distance,
  };
}

function getSegmentInwardNormal(start: Point, end: Point): Point {
  const dx = end.x - start.x;
  const dy = end.y - start.y;
  const length = Math.hypot(dx, dy);

  return {
    x: (-dy / length) * REGION_OUTLINE_INSET,
    y: (dx / length) * REGION_OUTLINE_INSET,
  };
}

function addPoints(firstPoint: Point, secondPoint: Point): Point {
  return {
    x: firstPoint.x + secondPoint.x,
    y: firstPoint.y + secondPoint.y,
  };
}

function pathPoint(point: Point): string {
  return `${point.x.toFixed(2)} ${point.y.toFixed(2)}`;
}

function isStraightPoint(previousPoint: Point, currentPoint: Point, nextPoint: Point): boolean {
  const previousDx = currentPoint.x - previousPoint.x;
  const previousDy = currentPoint.y - previousPoint.y;
  const nextDx = nextPoint.x - currentPoint.x;
  const nextDy = nextPoint.y - currentPoint.y;

  return previousDx * nextDy - previousDy * nextDx === 0;
}

function simplifyPathPoints(points: Point[]): Point[] {
  const uniquePoints =
    pointKey(points[0]) === pointKey(points[points.length - 1]) ? points.slice(0, -1) : points;

  return uniquePoints.filter((point, index) => {
    const previousPoint = uniquePoints[(index - 1 + uniquePoints.length) % uniquePoints.length];
    const nextPoint = uniquePoints[(index + 1) % uniquePoints.length];

    return !isStraightPoint(previousPoint, point, nextPoint);
  });
}

function getRoundedCornerPoints(points: Point[], index: number) {
  const previousPoint = toBoardPixels(points[(index - 1 + points.length) % points.length]);
  const currentPoint = toBoardPixels(points[index]);
  const nextPoint = toBoardPixels(points[(index + 1) % points.length]);

  const previousNormal = getSegmentInwardNormal(previousPoint, currentPoint);
  const nextNormal = getSegmentInwardNormal(currentPoint, nextPoint);

  const radius = Math.min(
    REGION_CORNER_RADIUS,
    Math.max(getDistance(previousPoint, currentPoint) / 2 - REGION_OUTLINE_INSET, 0),
    Math.max(getDistance(currentPoint, nextPoint) / 2 - REGION_OUTLINE_INSET, 0),
  );

  return {
    incomingPoint: addPoints(pointToward(currentPoint, previousPoint, radius), previousNormal),
    controlPoint: addPoints(addPoints(currentPoint, previousNormal), nextNormal),
    outgoingPoint: addPoints(pointToward(currentPoint, nextPoint, radius), nextNormal),
  };
}

function pathFromPoints(points: Point[]): string {
  const simplifiedPoints = simplifyPathPoints(points);
  const firstCorner = getRoundedCornerPoints(simplifiedPoints, 0);
  const commands = [`M ${pathPoint(firstCorner.outgoingPoint)}`];

  for (let index = 1; index <= simplifiedPoints.length; index += 1) {
    const corner = getRoundedCornerPoints(simplifiedPoints, index % simplifiedPoints.length);

    commands.push(
      `L ${pathPoint(corner.incomingPoint)}`,
      `Q ${pathPoint(corner.controlPoint)} ${pathPoint(corner.outgoingPoint)}`,
    );
  }

  commands.push("Z");

  return commands.join(" ");
}

function traceRegionPaths(edges: Edge[]): string[] {
  const outgoingEdges = new Map<string, Edge[]>();
  const unusedEdgeKeys = new Set<string>();

  for (const edge of edges) {
    const startKey = pointKey(edge.start);

    outgoingEdges.set(startKey, [...(outgoingEdges.get(startKey) ?? []), edge]);
    unusedEdgeKeys.add(edgeKey(edge));
  }

  const paths: string[] = [];

  while (unusedEdgeKeys.size > 0) {
    const firstEdgeKey = unusedEdgeKeys.values().next().value;
    const firstEdge = edges.find((edge) => edgeKey(edge) === firstEdgeKey);

    if (!firstEdge) {
      break;
    }

    const points = [firstEdge.start];
    let currentEdge = firstEdge;

    while (unusedEdgeKeys.has(edgeKey(currentEdge))) {
      unusedEdgeKeys.delete(edgeKey(currentEdge));
      points.push(currentEdge.end);

      if (pointKey(currentEdge.end) === pointKey(points[0])) {
        break;
      }

      const nextEdge = (outgoingEdges.get(pointKey(currentEdge.end)) ?? []).find((edge) =>
        unusedEdgeKeys.has(edgeKey(edge)),
      );

      if (!nextEdge) {
        break;
      }

      currentEdge = nextEdge;
    }

    if (points.length > 2) {
      paths.push(pathFromPoints(points));
    }
  }

  return paths;
}

export function getRegionOutlines(game: GameState): RegionOutline[] {
  const regionEdges = new Map<number, Edge[]>();

  for (let row = 0; row < game.size; row += 1) {
    for (let col = 0; col < game.size; col += 1) {
      const region = game.regions[row][col];
      const edges = regionEdges.get(region) ?? [];

      if (getRegionAt(game, row - 1, col) !== region) {
        addBoundaryEdge(edges, { x: col, y: row }, { x: col + 1, y: row });
      }

      if (getRegionAt(game, row, col + 1) !== region) {
        addBoundaryEdge(edges, { x: col + 1, y: row }, { x: col + 1, y: row + 1 });
      }

      if (getRegionAt(game, row + 1, col) !== region) {
        addBoundaryEdge(edges, { x: col + 1, y: row + 1 }, { x: col, y: row + 1 });
      }

      if (getRegionAt(game, row, col - 1) !== region) {
        addBoundaryEdge(edges, { x: col, y: row + 1 }, { x: col, y: row });
      }

      regionEdges.set(region, edges);
    }
  }

  return [...regionEdges.entries()].flatMap(([region, edges]) =>
    traceRegionPaths(edges).map((path) => ({ region, path })),
  );
}

export function getRegionCellPresentation(
  game: GameState,
  row: number,
  col: number,
): RegionCellPresentation {
  const region = game.regions[row][col];

  const hasTopEdge = getRegionAt(game, row - 1, col) !== region;
  const hasRightEdge = getRegionAt(game, row, col + 1) !== region;
  const hasBottomEdge = getRegionAt(game, row + 1, col) !== region;
  const hasLeftEdge = getRegionAt(game, row, col - 1) !== region;

  const horizontalGap = Number(hasLeftEdge) + Number(hasRightEdge);
  const verticalGap = Number(hasTopEdge) + Number(hasBottomEdge);
  const gapInset = REGION_GAP / 2;

  return {
    style: {
      marginTop: hasTopEdge ? gapInset : 0,
      marginRight: hasRightEdge ? gapInset : 0,
      marginBottom: hasBottomEdge ? gapInset : 0,
      marginLeft: hasLeftEdge ? gapInset : 0,
      width: `calc(${CELL_SIZE}px - ${horizontalGap * gapInset}px)`,
      height: `calc(${CELL_SIZE}px - ${verticalGap * gapInset}px)`,
    },
  };
}
