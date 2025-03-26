import json
import xml.dom.minidom as minidom
import xml.etree.ElementTree as ET
import os
import sys
import re

def read_file(file_path):
    """
    파일을 읽어서 내용을 반환합니다.
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()
        return content
    except Exception as e:
        print(f"파일 읽기 오류: {e}")
        sys.exit(1)

def fix_json_format(content):
    """
    잘못된 JSON 형식을 수정합니다.
    """
    # 특수한 형식의 JSON을 수정
    # [  "key": {...} ] 형식을 { "key": {...} } 형식으로 변환
    if content.strip().startswith('[') and '"' in content and ':' in content:
        # 첫 번째 큰따옴표로 둘러싸인 키 찾기
        match = re.search(r'\[\s*"([^"]+)":\s*\{', content)
        if match:
            key = match.group(1)
            # 대괄호 제거하고 올바른 JSON 형식으로 변환
            content = re.sub(r'\[\s*"([^"]+)":', f'{{"{key}":', content, 1)
            content = content.rstrip().rstrip(']') + '}'
    
    return content

def parse_input_file(content):
    """
    입력 파일 내용을 파싱합니다.
    """
    # JSON 형식 수정 시도
    fixed_content = fix_json_format(content)
    
    try:
        # 수정된 내용으로 JSON 파싱 시도
        data = json.loads(fixed_content)
        # 일관된 형식 유지를 위해 딕셔너리를 리스트로 감싸기
        if isinstance(data, dict):
            return [data]
        return data
    except json.JSONDecodeError as e:
        print(f"JSON 파싱 오류: {e}")
        # JSON 파싱 실패 시 대체 파싱 방법 시도
        try:
            result = parse_custom_format(content)
            return [result]  # 배열 형식으로 반환
        except Exception as e:
            print(f"커스텀 형식 파싱 오류: {e}")
            sys.exit(1)

def parse_custom_format(content):
    """
    커스텀 형식의 입력 내용을 파싱합니다.
    """
    lines = content.strip().split('\n')
    result = {}
    
    service_key = None
    record_key = None
    current_obj = None
    
    in_array = False
    in_service = False
    in_record = False
    
    stack = []
    
    for line in lines:
        line = line.strip()
        if not line or line.startswith('#'):
            continue
        
        # 배열 시작
        if line == '[':
            in_array = True
            stack.append('array')
        # 배열 끝
        elif line == ']':
            if stack and stack[-1] == 'array':
                stack.pop()
            in_array = len(stack) > 0 and stack[-1] == 'array'
        # 키와 객체 시작
        elif '"' in line and ':' in line and '{' in line:
            parts = line.split(':', 1)
            key = parts[0].strip().strip('"')
            
            if not in_service:
                # 서비스 키 발견
                service_key = key
                result[service_key] = {}
                in_service = True
                current_obj = result[service_key]
                stack.append('service')
            elif in_service and not in_record:
                # 레코드 키 발견
                record_key = key
                current_obj[record_key] = {}
                in_record = True
                current_obj = current_obj[record_key]
                stack.append('record')
        # 객체 끝
        elif line == '}' or line == '},':
            if stack:
                top = stack.pop()
                if top == 'record':
                    in_record = False
                    current_obj = result[service_key]
                elif top == 'service':
                    in_service = False
                    current_obj = result
        # 필드와 값
        elif '"' in line and ':' in line:
            parts = line.split(':', 1)
            key = parts[0].strip().strip('"')
            value = parts[1].strip().strip(',').strip()
            
            # 값에서 따옴표 제거
            if value.startswith('"') and value.endswith('"'):
                value = value[1:-1]
                
            if in_record and current_obj is not None:
                current_obj[key] = value
    
    return result

def create_xml_from_data(input_data, layout_data):
    """
    입력 데이터와 레이아웃 데이터를 기반으로 XML을 생성합니다.
    """
    root = ET.Element("root")
    
    # 디버깅을 위해 입력 데이터 출력
    print("XML 생성을 위한 입력 데이터:")
    print(json.dumps(input_data, indent=2))
    
    # 레이아웃 구조를 기반으로 XML 트리 생성
    for service_key, service_value in layout_data.items():
        service_elem = ET.SubElement(root, service_key)
        
        for record_key, record_value in service_value.items():
            record_elem = ET.SubElement(service_elem, record_key)
            
            for field_key in record_value.keys():
                field_elem = ET.SubElement(record_elem, field_key)
                
                # 입력 데이터에서 해당 필드 값 찾기
                try:
                    # 입력 데이터 구조 확인
                    if isinstance(input_data, list):
                        # 리스트인 경우
                        for item in input_data:
                            if service_key in item:
                                if record_key in item[service_key]:
                                    if field_key in item[service_key][record_key]:
                                        value = item[service_key][record_key][field_key]
                                        if value is not None and value != "":
                                            field_elem.text = str(value)
                                            break
                    elif isinstance(input_data, dict):
                        # 딕셔너리인 경우
                        if service_key in input_data:
                            if record_key in input_data[service_key]:
                                if field_key in input_data[service_key][record_key]:
                                    value = input_data[service_key][record_key][field_key]
                                    if value is not None and value != "":
                                        field_elem.text = str(value)
                except (KeyError, IndexError, TypeError) as e:
                    # 오류 발생 시 빈 값으로 남겨둠
                    pass
    
    # XML 문자열로 변환 및 들여쓰기 적용
    xml_str = ET.tostring(root, encoding='utf-8')
    dom = minidom.parseString(xml_str)
    pretty_xml = dom.toprettyxml(indent="  ")
    
    return pretty_xml

def ensure_directory_exists(directory_path):
    """
    디렉토리가 존재하지 않으면 생성합니다.
    """
    if not os.path.exists(directory_path):
        try:
            os.makedirs(directory_path)
            print(f"디렉토리 생성: {directory_path}")
        except Exception as e:
            print(f"디렉토리 생성 오류: {e}")
            sys.exit(1)

def main():
    # 파일 경로 설정
    input_file_path = "testservice_input_files/SPA0A039P01_input.txt"
    layout_file_path = "testservice_layout_files/SPA0A039P01_layouts.json"
    result_dir = "testservice_result_files"
    output_file_path = os.path.join(result_dir, "SPA0A039P01_output.xml")
    
    # 결과 디렉토리 확인 및 생성
    ensure_directory_exists(result_dir)
    
    # 입력 파일이 존재하는지 확인
    if not os.path.exists(input_file_path):
        print(f"입력 파일이 존재하지 않습니다: {input_file_path}")
        sys.exit(1)
    
    # 레이아웃 파일이 존재하는지 확인
    if not os.path.exists(layout_file_path):
        print(f"레이아웃 파일이 존재하지 않습니다: {layout_file_path}")
        sys.exit(1)
    
    # 파일 읽기
    input_content = read_file(input_file_path)
    layout_content = read_file(layout_file_path)
    
    print("입력 파일 내용:")
    print(input_content)
    
    # 입력 데이터 파싱
    try:
        input_data = parse_input_file(input_content)
        print("\n파싱된 입력 데이터:")
        print(json.dumps(input_data, indent=2))
    except Exception as e:
        print(f"입력 데이터 파싱 오류: {e}")
        # 입력 파일 수동 수정을 위한 대체 방법
        print("\n입력 파일 형식이 잘못되었습니다. 다음과 같이 형식을 수정해 보세요:")
        fixed_content = input_content.replace('[', '{', 1).replace(']', '}', 1)
        fixed_content = fixed_content.replace('"SPA0A0390P01In":', '"SPA0A0390P01In":', 1)
        print(fixed_content)
        sys.exit(1)
    
    # 레이아웃 데이터 파싱
    try:
        layout_data = json.loads(layout_content)
        print("\n파싱된 레이아웃 데이터:")
        print(json.dumps(layout_data, indent=2))
    except json.JSONDecodeError as e:
        print(f"레이아웃 데이터 파싱 오류: {e}")
        sys.exit(1)
    
    # XML 생성
    xml_output = create_xml_from_data(input_data, layout_data)
    
    # 결과 파일 저장
    with open(output_file_path, 'w', encoding='utf-8') as file:
        file.write(xml_output)
    
    print(f"\nXML 파일이 생성되었습니다: {output_file_path}")
    print("생성된 XML 내용:")
    print(xml_output)

if __name__ == "__main__":
    main()
