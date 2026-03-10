'use client';

import { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, useMap, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix lỗi icon Marker mặc định của Leaflet trong Next.js
const customIcon = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

interface MapSectionProps {
  center: [number, number];
  markerPos: [number, number] | null;
  onMapClick: (lat: number, lng: number) => void;
}

// Hỗ trợ bay bản đồ mượt mà
function MapController({ center }: { center: [number, number] }) {
  const map = useMap();
  useEffect(() => {
    map.flyTo(center, 14, { animate: true, duration: 1.5 });
  }, [center, map]);
  return null;
}

// Xử lý click và fix kích thước khi Dialog bung ra
function MapInternalEvents({ onSelect }: { onSelect: (lat: number, lng: number) => void }) {
  const map = useMap();
  
  useEffect(() => {
    const timer = setTimeout(() => {
      map.invalidateSize();
    }, 400); // Đợi Dialog animation hoàn tất
    return () => clearTimeout(timer);
  }, [map]);

  useMapEvents({
    click(e) {
      onSelect(e.latlng.lat, e.latlng.lng);
    },
  });

  return null;
}

export default function MapSection({ center, markerPos, onMapClick }: MapSectionProps) {
  return (
    <MapContainer 
      center={center} 
      zoom={13} 
      style={{ height: '100%', width: '100%' }}
      trackResize={true}
    >
      <TileLayer 
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OSM</a>'
      />
      <MapController center={center} />
      <MapInternalEvents onSelect={onMapClick} />
      {markerPos && <Marker position={markerPos} icon={customIcon} />}
    </MapContainer>
  );
}